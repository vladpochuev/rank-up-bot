package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandFactory {
    private final TelegramBot bot;

    public Command getCommand(CommandType command) {
        if (command == null) return new UnknownCommand(bot);

        switch (command) {
            case START -> {
                return new StartCommand(bot);
            }
        }
        return new UnknownCommand(bot);
    }
}