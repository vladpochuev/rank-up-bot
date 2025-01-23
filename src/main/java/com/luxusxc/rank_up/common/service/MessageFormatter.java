package com.luxusxc.rank_up.common.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageFormatter {
    private final static String DELIMITER = "\t";
    private final static String LINE_BREAK = "\n";

    private final StringSplitter splitter;

    public String alignMessage(String message, int maxWidth) {
        return splitter.split(message, LINE_BREAK).stream()
                .map(string -> getString(string, maxWidth))
                .collect(Collectors.joining());
    }

    private String getString(String string, int maxWidth) {
        if (isPlainString(string)) {
            return string + LINE_BREAK;
        } else {
            return getAlignedString(string, maxWidth);
        }
    }

    private boolean isPlainString(String string) {
        return !string.contains(DELIMITER);
    }

    private String getAlignedString(String string, int maxWidth) {
        List<String> keyValue = splitter.split(string, DELIMITER);
        String key = keyValue.get(0);
        String value = keyValue.get(1);
        int diff = maxWidth - key.length() - value.length();
        return key + " ".repeat(diff) + value + LINE_BREAK;
    }
}
