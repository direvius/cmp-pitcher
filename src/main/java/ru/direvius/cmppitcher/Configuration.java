/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.direvius.cmppitcher;

import java.io.OutputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author direvius
 */
public class Configuration {
    private final static Logger logger = LoggerFactory.getLogger(Configuration.class);
    private static final Configuration INSTANCE;
    private static final Properties DEFAULTS;
    static{
        DEFAULTS = new Properties();
        DEFAULTS.setProperty("thread_count", "50");
        DEFAULTS.setProperty("first_card_number", "100000000000000");
        DEFAULTS.setProperty("cmp_ip", "10.0.3.70");
        System.setProperty("java.net.preferIPv4Stack", "true");
        DEFAULTS.putAll(System.getProperties());
        logger.debug(DEFAULTS.toString().replace(",", "\n"));
        
        INSTANCE = new Configuration();
    }
    public static Configuration get(){ return INSTANCE; }
    private final int threadCount;
    private final long firstCardNumber;
    private final String serverIP;
    private Configuration(){
        threadCount = Integer.parseInt(DEFAULTS.getProperty("thread_count"));
        firstCardNumber = Long.parseLong(DEFAULTS.getProperty("first_card_number"));
        serverIP = DEFAULTS.getProperty("cmp_ip");
    }
    public int threadCount(){ return threadCount; }
    public long firstCardNumber(){ return firstCardNumber; }
    public String serverIP(){ return serverIP; }
}
