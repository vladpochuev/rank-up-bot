package com.luxusxc.mirror_tg_ds.commands;

import com.luxusxc.mirror_tg_ds.service.TelegramBot;
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
            default -> {
                return new UnknownCommand(bot);
            }
        }
    }
}