package com.luxusxc.rank_up.telegram;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@AllArgsConstructor
public class DecisionCenter {
    private final JoinChatProcessor joinChatProcessor;
    private final ChatMessageProcessor chatMessageProcessor;
    private final CommandProcessor commandProcessor;
    private final CallbackProcessor callbackProcessor;

    public BotAction processUpdate(Update update) {
        if (update.hasMyChatMember()) {
            return processIfJoinChat(update.getMyChatMember());
        } else if (update.hasMessage()) {
            return processMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            return processCallback(update.getCallbackQuery());
        }
        return null;
    }

    private BotAction processIfJoinChat(ChatMemberUpdated memberUpdated) {
        return joinChatProcessor.updateChatInfo(memberUpdated);
    }

    private BotAction processMessage(Message message) {
        if (!message.hasText()) return null;

        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat()) {
            return chatMessageProcessor.processMessage(message);
        } else if (message.getChat().isUserChat()) {
            return commandProcessor.processCommand(message);
        }
        return null;
    }

    private BotAction processCallback(CallbackQuery callback) {
        return callbackProcessor.processCallback(callback);
    }
}
