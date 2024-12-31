package com.luxusxc.rank_up.telegram.commands;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public enum CommandType {
    START("/start", "Start using the bot", false),
    HELP("/help", "Show the list of commands", false),
    CHAT_LIST("/chatlist", "Show the list of chats with your statistics in them", false),
    STATS("/stats", "Show general group statistics", true);

    public final String body;
    public final String description;
    public final boolean isForGroup;

    private static final Map<String, CommandType> BODY_TO_USER_COMMAND;
    private static final Map<String, CommandType> BODY_TO_GROUP_COMMAND;

    static {
        BODY_TO_USER_COMMAND = new HashMap<>();
        BODY_TO_GROUP_COMMAND = new HashMap<>();
        for (CommandType command : values()) {
            if (command.isForGroup) {
                BODY_TO_GROUP_COMMAND.put(command.body, command);
            } else {
                BODY_TO_USER_COMMAND.put(command.body, command);
            }
        }
    }

    public static CommandType getUserInstance(String body) {
        return BODY_TO_USER_COMMAND.get(body);
    }

    public static CommandType getGroupInstance(String body) {
        return BODY_TO_GROUP_COMMAND.get(body);
    }

    public static List<CommandType> getUserCommands() {
        return Arrays.stream(values()).filter(c -> !c.isForGroup).toList();
    }

    public static List<CommandType> getGroupCommands() {
        return Arrays.stream(values()).filter(c -> c.isForGroup).toList();
    }
}

