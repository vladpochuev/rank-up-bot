package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public class HelpCommand extends Command {
    private static final String HELP_MESSAGE = """
            This bot can be used to determine a group user's rank based on the number of messages they have sent.
            To start using the bot, add it to a group and promote it to admin.
                        
            <b>User direct commands</b>
            /start - start using the bot
            /help - show the list of commands
            /chatlist - show the list of chats with your statistics in them
                        
            <b>Group commands</b>
            /stats - show general group statistics
            """;
    private static final String LOG_TEMPLATE = "Help command was called (id=%d)";
    private static final String SUCCESS_LOG_TEMPLATE = "User has received the help message (id=%d)";

    public HelpCommand(TelegramBot bot) {
        super(bot);
    }


    @Override
    public void executeCommand(Message message) {
        long chatId = message.getChatId();
        log.info(LOG_MARKER, LOG_TEMPLATE.formatted(chatId));
        bot.sendMessage(chatId, HELP_MESSAGE, "HTML");
        log.info(LOG_MARKER, SUCCESS_LOG_TEMPLATE.formatted(chatId));
    }
}
