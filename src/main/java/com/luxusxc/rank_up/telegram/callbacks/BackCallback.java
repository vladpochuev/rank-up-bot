package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.mapper.CallbackQueryMapper;
import com.luxusxc.rank_up.telegram.TelegramBot;
import com.luxusxc.rank_up.telegram.commands.ChatListCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public class BackCallback extends Callback {
    public BackCallback(TelegramBot bot) {
        super(bot);
    }

    @Override
    protected void executeCallback(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        sendChatList(callbackQuery);
    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getFrom().getId();
        int messageId = callbackQuery.getMessage().getMessageId();
        bot.deleteMessage(chatId, messageId);
    }

    private void sendChatList(CallbackQuery callbackQuery) {
        ChatListCommand chatListCommand = new ChatListCommand(bot);
        Message message = mapMessage(callbackQuery);
        chatListCommand.executeCommand(message);
    }

    private Message mapMessage(CallbackQuery callbackQuery) {
        CallbackQueryMapper mapper = bot.getCallbackQueryMapper();
        return mapper.toMessage(callbackQuery);
    }
}
