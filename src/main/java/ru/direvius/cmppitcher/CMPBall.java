package ru.direvius.cmppitcher;

import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.direvius.cmp.CMPClient;
import ru.direvius.cmp.CardInfoRequestBuilder;
import ru.direvius.pitcher.Ball;

/**
 *
 * @author direvius
 */
public class CMPBall implements Ball {

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }
    private static final ExecutorService es = Executors.newFixedThreadPool(100);

    public void beforeStart() {
    }

    public void afterFinished() {
    }

    public void run() {
        es.submit(new Runnable() {

            public void run() {
                try {
                    long cardNumber = CardNumberGenerator.getInstance().getCardNumber();
                    int terminalID = TerminalIDGenerator.getInstance().getID();
                    Socket s = new Socket("10.0.3.70", 688);
                    CMPClient cmpClient = new CMPClient(s.getInputStream(), s.getOutputStream());
                    cmpClient.open();
                    cmpClient.sendEncrypt(new CardInfoRequestBuilder(terminalID, cardNumber, new Date()).build());
                    cmpClient.receiveDecrypt();
                    cmpClient.close();
                    s.close();
                } catch (IOException ex) {
                    Logger.getLogger(CMPBall.class.getName()).log(Level.SEVERE, null, ex);
                } catch (GeneralSecurityException ex) {
                    Logger.getLogger(CMPBall.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }
}
