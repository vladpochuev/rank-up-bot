package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.config.BotConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommandParser {
    private final static String DELIMITER = " ";
    private final StringSplitter splitter;
    private final BotConfig botConfig;

    public String getCommandBody(String message) {
        List<String> splitMessage = splitter.split(message, DELIMITER);
        throwIfEmpty(splitMessage);
        String commandBody = splitMessage.get(0);
        commandBody = isDirectCommand(commandBody) ? getCommonCommand(commandBody) : commandBody;
        return commandBody;
    }

    private boolean isDirectCommand(String command) {
        return command.matches("/.+@" + botConfig.getBotName());
    }

    private String getCommonCommand(String directCommand) {
        return splitter.split(directCommand, "@").get(0);
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
