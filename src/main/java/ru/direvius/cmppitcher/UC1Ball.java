/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.direvius.cmppitcher;

import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    TransactionLogger tl = TransactionLogger.get();

    public void beforeStart() {
    }

    public void afterFinished() {
    }

    public void run() {
        try {
            long cardNumber = CardNumberGenerator.getInstance().getCardNumber();
            int terminalID = TerminalIDGenerator.getInstance().getID();
            Socket s = new Socket("10.0.3.70", 688);
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
            Logger.getLogger(UC1Ball.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CMPBall.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(CMPBall.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
