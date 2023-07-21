package com.mindhub.homebankingPrueba.utils;

import java.util.Random;

public final class CardUtils {

    private CardUtils(){}

    public static short getRandomNumberCvv(short minCvv, short maxCvv) {
        return (short) ((Math.random() * (maxCvv - minCvv)) + minCvv);
    }

    public static String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardNumber.append(random.nextInt(10));
            }
            if (i < 3) {
                cardNumber.append("-");
            }
        }

        return cardNumber.toString();
    }
}
