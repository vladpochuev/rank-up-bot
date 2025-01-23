package com.luxusxc.rank_up.telegram.command;

import com.luxusxc.rank_up.telegram.service.TelegramBot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandFactory {
    private final TelegramBot bot;

    public Command getCommand(CommandType command) {
        if (command == null) return new UnknownCommand(bot);

        switch (command) {
            case START -> {
                return new StartCommand(bot);
            } case HELP -> {
                return new HelpCommand(bot);
            } case CHAT_LIST -> {
                return new ChatListCommand(bot);
            } case STATS -> {
                return new StatsCommand(bot);
            }
        }
        return new UnknownCommand(bot);
    }
}