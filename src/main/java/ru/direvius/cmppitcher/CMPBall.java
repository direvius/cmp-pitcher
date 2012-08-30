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

    private static final ExecutorService es = Executors.newFixedThreadPool(Configuration.get().threadCount());
    public void beforeStart() {
    }

    public void afterFinished() {
    }

    public void run() {
        es.submit(new Runnable() {

            public void run() {
                new UC1Ball().run();
            }
        });

    }
}
