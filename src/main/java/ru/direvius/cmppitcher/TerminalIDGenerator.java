/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.direvius.cmppitcher;

import java.util.Random;

/**
 *
 * @author direvius
 */
public class TerminalIDGenerator {
    private TerminalIDGenerator(){}
    private static final TerminalIDGenerator instance = new TerminalIDGenerator();
    private static final Random r = new Random();
    public static TerminalIDGenerator getInstance(){
        return instance;
    }
    public int getID(){
        return r.nextInt(4999)+7775000;
    }
}
