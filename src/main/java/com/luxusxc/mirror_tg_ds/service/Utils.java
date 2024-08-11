package com.luxusxc.mirror_tg_ds.service;

import java.util.Arrays;

public class Utils {

    public static String getCommandBody(String message) {
        return message.split(" ")[0];
    }

    public static String[] getArgs(String message) {
        String[] splitedString = message.split(" ");
        return Arrays.copyOfRange(splitedString, 1, splitedString.length);
    }
}