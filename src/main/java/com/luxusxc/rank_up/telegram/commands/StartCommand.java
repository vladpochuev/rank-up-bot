package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public class StartCommand extends Command {
    private static final String WELCOME_MESSAGE_FORMAT = """
            Hi, %s, nice to meet you!
            
            This bot can be used to determine a group user's rank based on the number of messages they have sent.
            To start using the bot, add it to a group and promote it to admin.
            
            <b>User direct commands</b>
            /start - start using the bot
            /help - show the list of commands
            /chatlist - show the list of chats with your statistics in them
            
            <b>Group commands</b>
            /stats - show general group statistics
            """;

    public StartCommand(TelegramBot bot) {
        super(bot);
    }

    public void execute(Message message) {
        String firstName = message.getFrom().getFirstName();
        String response = WELCOME_MESSAGE_FORMAT.formatted(firstName);
        bot.sendMessage(message.getChatId(), response, "HTML");
    }
}
