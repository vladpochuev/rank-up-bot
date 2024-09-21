package com.luxusxc.rank_up.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandParser {
    private final static String DELIMITER = " ";
    private final StringSplitter splitter;

    @Autowired
    public CommandParser(StringSplitter splitter) {
        this.splitter = splitter;
    }

    public String getCommandBody(String message) {
        List<String> splitMessage = splitter.split(message, DELIMITER);
        if (splitMessage.equals(List.of())) {
            throw new IllegalArgumentException("Provided message is empty");
        }
        return splitMessage.get(0);
    }

    public List<String> getArgs(String message) {
        List<String> splitMessage = splitter.split(message, DELIMITER);
        if (splitMessage.equals(List.of())) {
            throw new IllegalArgumentException("Provided message is empty");
        }
        return splitMessage.subList(1, splitMessage.size());
    }
}