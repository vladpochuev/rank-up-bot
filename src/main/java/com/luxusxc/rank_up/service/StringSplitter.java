package com.luxusxc.rank_up.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StringSplitter {
    public List<String> split(String string, String delimiter) {
        throwIfNull(string, delimiter);
        if (isEmpty(delimiter)) return List.of(string);
        if (isEmpty(string)) return List.of();

        return splitString(string, delimiter);
    }

    private void throwIfNull(String string, String delimiter) {
        if (string == null || delimiter == null) {
            throw new IllegalArgumentException("Provided string or delimiter is null");
        }
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

    private List<String> splitString(String string, String delimiter) {
        String trimmedString = string.trim();
        String singleDelimiterString = trimmedString.replaceAll(delimiter + "+", delimiter);
        String[] splitString = singleDelimiterString.split(delimiter);
        return List.of(splitString);
    }
}
