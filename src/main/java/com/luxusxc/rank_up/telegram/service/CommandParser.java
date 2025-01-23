package com.luxusxc.rank_up.telegram.service;

import com.luxusxc.rank_up.telegram.config.BotConfig;
import com.luxusxc.rank_up.common.service.StringSplitter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommandParser {
    private final static String DELIMITER = " ";
    private final static String EXCEPTION_MESSAGE = "Provided message is empty";

    private final StringSplitter splitter;
    private final BotConfig botConfig;

    public String getCommandBody(String message) {
        List<String> splitMessage = splitter.split(message, DELIMITER);
        throwIfEmpty(splitMessage);
        return formatCommandBody(splitMessage.get(0));
    }

    private String formatCommandBody(String commandBody) {
        if (isDirectCommand(commandBody)) {
            return getCommonCommandBody(commandBody);
        } else {
            return commandBody;
        }
    }

    private boolean isDirectCommand(String command) {
        return command.matches("/.+@" + botConfig.getBotName());
    }

    private String getCommonCommandBody(String directCommand) {
        return splitter.split(directCommand, "@").get(0);
    }

    public List<String> getArgs(String message) {
        List<String> splitMessage = splitter.split(message, DELIMITER);
        throwIfEmpty(splitMessage);
        return splitMessage.subList(1, splitMessage.size());
    }

    private void throwIfEmpty(List<String> messages) {
        if (messages.equals(List.of())) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE);
        }
    }
}
