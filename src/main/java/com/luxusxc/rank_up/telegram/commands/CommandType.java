package com.luxusxc.rank_up.telegram.commands;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum CommandType {
    START("/start", "Start using the bot"),
    CHAT_LIST ( "/chatlist", "View the chats where you have any ranks");

    public final String body;
    public final String description;
    private static final Map<String, CommandType> BODY_TO_COMMAND;

    static {
        BODY_TO_COMMAND = new HashMap<>();
        for (CommandType command : values()) {
            BODY_TO_COMMAND.put(command.body, command);
        }
    }

    public static CommandType getInstance(String body) {
        return BODY_TO_COMMAND.get(body);
    }
}

