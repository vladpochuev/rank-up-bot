package com.luxusxc.rank_up.service;

import org.springframework.stereotype.Service;

@Service
public class MessageFormatter {
    private final static String DELIMITER_REGEX = "\\t";

    public String alignMessage(String message, int maxWidth) {
        StringBuilder builder = new StringBuilder();
        String[] strings = message.split("\n");
        for (String string : strings) {
            String[] keyValue = string.split(DELIMITER_REGEX);
            if (keyValue.length < 2) {
                builder.append(string).append("\n");
            } else {
                int diff = maxWidth - keyValue[0].length() - keyValue[1].length();
                builder.append(keyValue[0]).append(" ".repeat(diff)).append(keyValue[1]).append("\n");
            }
        }
        return builder.toString();
    }
}
