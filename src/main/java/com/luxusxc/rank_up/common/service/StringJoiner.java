package com.luxusxc.rank_up.common.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StringJoiner {
    private static final String EXCEPTION_LOG = "Provided strings or delimiter has null value";

    public String join(List<String> strings, String delimiter) {
        throwIfNull(strings, delimiter);
        return String.join(delimiter, strings);
    }

    private void throwIfNull(List<String> strings, String delimiter) {
        if (strings == null || delimiter == null || containsNull(strings)) {
            throw new IllegalArgumentException(EXCEPTION_LOG);
        }
    }

    private boolean containsNull(List<String> strings) {
        for (String string : strings) {
            if (string == null) return true;
        }
        return false;
    }
}
