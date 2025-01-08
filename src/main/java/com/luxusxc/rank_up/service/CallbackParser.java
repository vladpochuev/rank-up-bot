package com.luxusxc.rank_up.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CallbackParser {
    private final static String DELIMITER = "_";
    private final StringSplitter splitter;

    public String getPrefix(String message) {
        List<String> splitMessage = splitter.split(message, DELIMITER);
        throwIfEmpty(splitMessage);
        return splitMessage.get(0);
    }

    public List<String> getArgs(String message) {
        List<String> splitMessage = splitter.split(message, DELIMITER);
        throwIfEmpty(splitMessage);
        return splitMessage.subList(1, splitMessage.size());
    }

    private void throwIfEmpty(List<String> messages) {
        if (messages.equals(List.of())) {
            throw new IllegalArgumentException("Provided message is empty");
        }
    }
}
