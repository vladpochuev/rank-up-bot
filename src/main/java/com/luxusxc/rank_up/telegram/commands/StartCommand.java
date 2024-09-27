package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public class StartCommand extends Command {

    public StartCommand(TelegramBot bot) {
        super(bot);
    }

    public void execute(Message message) {}
}
