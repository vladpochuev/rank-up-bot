package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UnknownCommand extends Command {
    private static final String MESSAGE = "Sorry, command was not recognized.";

    public UnknownCommand(TelegramBot bot) {
        super(bot);
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        bot.sendMessage(chatId, MESSAGE);
    }
}
