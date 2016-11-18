package com.itsnowball.educraftadmin;

/**
 * Created by Ulnamsong on 2016. 11. 13..
 */

public class Data {
    public static String version = "V0001";

    public static String generateRandomValue() {
        int random = (int) (Math.random() * 999999);
        String result = random + "";
        return result;
    }
}
