package com.luxusxc.rank_up.telegram.command;

import com.luxusxc.rank_up.telegram.service.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public class StartCommand extends Command {
    private static final String WELCOME_MESSAGE_TEMPLATE = """
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
    private static final String SUCCESS_LOG_TEMPLATE = "User has received start message (id=%d)";

    public StartCommand(TelegramBot bot) {
        super(bot);
    }

    public void executeCommand(Message message) {
        long chatId = message.getChatId();
        String firstName = message.getFrom().getFirstName();
        String response = WELCOME_MESSAGE_TEMPLATE.formatted(firstName);

        bot.sendMessage(chatId, response, "HTML");
        log.info(LOG_MARKER, SUCCESS_LOG_TEMPLATE.formatted(chatId));
    }
}
