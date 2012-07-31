/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.direvius.cmppitcher;

/**
 *
 * @author direvius
 */
public class CardNumberGenerator {
    private static long cardNumber = 100000000000000L;
    private CardNumberGenerator(){}
    private final static CardNumberGenerator instance = new CardNumberGenerator();
    public static CardNumberGenerator getInstance(){
        return instance;
    }
    public synchronized long getCardNumber(){
        return cardNumber++;
    }
}
