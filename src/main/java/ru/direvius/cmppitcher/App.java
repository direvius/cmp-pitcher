package ru.direvius.cmppitcher;

import java.util.logging.Level;
import java.util.logging.Logger;
import ru.direvius.pitcher.Pitcher;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Pitcher p = new Pitcher(new CMPBall());
        p.begin(1);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ru.direvius.pitcher.App.class.getName()).log(Level.SEVERE, null, ex);
        }
        p.setRate(50);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ru.direvius.pitcher.App.class.getName()).log(Level.SEVERE, null, ex);
        }
        p.finish();
        System.exit(0);
    }
}
