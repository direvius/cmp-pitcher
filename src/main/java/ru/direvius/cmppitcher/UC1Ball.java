/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.direvius.cmppitcher;

import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.direvius.cmp.CMPClient;
import ru.direvius.cmp.requests.BonusRequest;
import ru.direvius.cmp.requests.CardInfoRequest;
import ru.direvius.cmp.requests.CloseCheckRequest;
import ru.direvius.cmp.requests.RequestFactory;
import ru.direvius.pitcher.Ball;
import ru.direvius.pitcher.Stopwatch;
import ru.direvius.pitcher.TransactionLogger;

/**
 *
 * @author direvius
 */
public class UC1Ball implements Ball {
    private final static Logger logger = LoggerFactory.getLogger(TransactionLogger.class);
    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }
    TransactionLogger tl = TransactionLogger.get();

    public void beforeStart() {
    }

    public void afterFinished() {
    }

    public void run() {
        try {
            long cardNumber = CardNumberGenerator.getInstance().getCardNumber();
            int terminalID = TerminalIDGenerator.getInstance().getID();
            String ip = System.getProperty("cmp.ip");
            if(ip == null) ip = "10.0.3.70";
            logger.debug("IP: {}", ip);
            Socket s = new Socket(ip, 688);
            CMPClient cmpClient = new CMPClient(s.getInputStream(), s.getOutputStream());
            cmpClient.open();
            
            Stopwatch swAfterOpen = new Stopwatch();
            cmpClient.sendEncrypt(new CardInfoRequest(terminalID, cardNumber, new Date()).asByteArray());
            cmpClient.receiveDecrypt();
            tl.log("card_info", swAfterOpen.measure(), "OK");
            
            Thread.sleep(5000);
            
            Stopwatch swAfterInfo = new Stopwatch();
            cmpClient.sendEncrypt(new BonusRequest(new Date(), RequestFactory.get(RequestFactory.SampleType.checkModifyFuel)).asByteArray());
            cmpClient.receiveDecrypt();
            tl.log("bonus_buy", swAfterInfo.measure(), "OK");
            
            Thread.sleep(5000);
            
            Stopwatch swAfterBuy = new Stopwatch();
            cmpClient.sendEncrypt(new CloseCheckRequest(RequestFactory.get(RequestFactory.SampleType.closeCheck)).asByteArray());
            cmpClient.receiveDecrypt();
            tl.log("close_trn", swAfterBuy.measure(), "OK");
            
            cmpClient.close();
            s.close();
        } catch (InterruptedException ex) {
            logger.error("Interrupted while running a scenario", ex);
        } catch (IOException ex) {
            logger.error("IO problem while running a scenario", ex);
        } catch (GeneralSecurityException ex) {
            logger.error("Crypto problem while running a scenario", ex);
        }
    }
}
