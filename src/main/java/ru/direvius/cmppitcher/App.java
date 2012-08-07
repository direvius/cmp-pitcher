package ru.direvius.cmppitcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.direvius.pitcher.Pitcher;

/**
 * Hello world!
 *
 */
public class App {
    public static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        int time = Integer.parseInt(args[0]);
        int rps = Integer.parseInt(args[1]);
        logger.info("time: {}, rps: {}", time, rps);
        Pitcher p = new Pitcher(new UC1Ball());
        p.begin(rps);
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException ex) {
            logger.error("Interrupted", ex);
        }
        p.finish();
        System.exit(0);
    }
}
