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
        int time = Integer.parseInt(args[0]);
        int rps = Integer.parseInt(args[1]);
        Pitcher p = new Pitcher(new UC1Ball());
        p.begin(rps);
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ru.direvius.pitcher.App.class.getName()).log(Level.SEVERE, null, ex);
        }
        p.finish();
        System.exit(0);
    }
}
