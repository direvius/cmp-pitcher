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
            String ip = Configuration.get().serverIP();
            int port = Configuration.get().serverPort();
            Socket s = new Socket(ip, port);
            CMPClient cmpClient = new CMPClient(s.getInputStream(), s.getOutputStream());
            cmpClient.open();
            
            Stopwatch swAfterOpen = new Stopwatch();
            logger.info("card_info started, card {}", cardNumber);
            cmpClient.sendEncrypt(new CardInfoRequest(terminalID, cardNumber, new Date()).asByteArray());
            logger.info("card_info sent, card {}", cardNumber);
            cmpClient.receiveDecrypt();
            
            logger.info("card_info received, card {}", cardNumber);
            tl.log("card_info", swAfterOpen.measure(), "OK");
            
            Thread.sleep(2000);
            cmpClient.keepAlive();
            Thread.sleep(2000);
            cmpClient.keepAlive();
            Thread.sleep(1000);
            
            Stopwatch swAfterInfo = new Stopwatch();
            
            logger.info("bonus_buy started, card {}", cardNumber);
            cmpClient.sendEncrypt(new BonusRequest(new Date(), RequestFactory.get(RequestFactory.SampleType.checkModifyFuel)).asByteArray());
            
            logger.info("bonus_buy sent, card {}", cardNumber);
            cmpClient.receiveDecrypt();
            
            logger.info("bonus_buy received, card {}", cardNumber);
            tl.log("bonus_buy", swAfterInfo.measure(), "OK");
            
            Thread.sleep(2000);
            cmpClient.keepAlive();
            Thread.sleep(2000);
            cmpClient.keepAlive();
            Thread.sleep(1000);
            
            Stopwatch swAfterBuy = new Stopwatch();
            
            logger.info("close_trn started, card {}", cardNumber);
            cmpClient.sendEncrypt(new CloseCheckRequest(RequestFactory.get(RequestFactory.SampleType.closeCheck)).asByteArray());
            
            logger.info("close_trn sent, card {}", cardNumber);
            cmpClient.receiveDecrypt();
            
            logger.info("close_trn received, card {}", cardNumber);
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
