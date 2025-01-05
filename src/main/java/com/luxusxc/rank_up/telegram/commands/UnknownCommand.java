package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.service.CommandParser;
import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
public class UnknownCommand extends Command {
    private static final String MESSAGE = "Sorry, command was not recognized.";
    private static final String UNKNOWN_LOG_TEMPLATE = "User was notified that command '%s' does not exists (id=%d)";
    private static final String UNKNOWN_LOG_DEFAULT_TEMPLATE = "Command wasn't recognized (id=%d)";

    public UnknownCommand(TelegramBot bot) {
        super(bot);
    }

    @Override
    public void executeCommand(Message message) {
        long chatId = message.getChatId();

        try {
            CommandParser parser = bot.getCommandParser();
            String commandBody = parser.getCommandBody(message.getText());
            sendCommandMessage(chatId, commandBody);
        } catch (Exception e) {
            sendDefaultMessage(chatId);
        }
    }

    private void sendCommandMessage(long chatId, String commandBody) {
        bot.sendMessage(chatId, MESSAGE);
        log.info(LOG_MARKER, UNKNOWN_LOG_TEMPLATE.formatted(commandBody, chatId));
    }

    private void sendDefaultMessage(long chatId) {
        bot.sendMessage(chatId, MESSAGE);
        log.error(LOG_MARKER, UNKNOWN_LOG_DEFAULT_TEMPLATE.formatted(chatId));
    }
}
