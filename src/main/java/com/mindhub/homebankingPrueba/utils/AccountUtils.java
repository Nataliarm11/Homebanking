package com.mindhub.homebankingPrueba.utils;

public class AccountUtils {

    private AccountUtils(){}

    public static int getRandomNumberAccount(int minAccount, int maxAccount) {
        return (int) ((Math.random() * (maxAccount - minAccount)) + minAccount);
    }
}
