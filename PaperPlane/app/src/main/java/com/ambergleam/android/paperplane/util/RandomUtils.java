package com.ambergleam.android.paperplane.util;

import java.util.Random;

public class RandomUtils {

    public static int generateRandomValue(int value) {
        return new Random().nextInt(2 * value + 1) - value;
    }

}
