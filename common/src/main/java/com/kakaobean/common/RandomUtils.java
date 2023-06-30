package com.kakaobean.common;

import java.util.Random;

public class RandomUtils {

    private RandomUtils() {}

    public static String creatRandomKey(){
        Random random = new Random();
        return String.valueOf(random.nextInt(888888) + 111111);
    }
}
