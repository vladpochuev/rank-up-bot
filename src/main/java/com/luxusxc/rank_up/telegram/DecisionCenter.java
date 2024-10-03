package com.luxusxc.rank_up.telegram;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Service
@AllArgsConstructor
public class DecisionCenter {
    private final BotJoinChatProcessor botJoinChatProcessor;
    private final BotLeftChatProcessor botLeftChatProcessor;
    private final UserLeftChatProcessor userLeftChatProcessor;
    private final ChatMessageProcessor chatMessageProcessor;
    private final CommandProcessor commandProcessor;
    private final CallbackProcessor callbackProcessor;

    public BotAction processUpdate(Update update) {
        if (update.hasMyChatMember()) {
            return processBotUpdated(update.getMyChatMember());
        } else if (update.hasChatMember()) {
            return processUserUpdated(update.getChatMember());
        } else if (update.hasMessage()) {
            return processMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            return processCallback(update.getCallbackQuery());
        }
        return null;
    }

    private BotAction processBotUpdated(ChatMemberUpdated botUpdated) {
        if (isLeft(botUpdated) || isKicked(botUpdated)) {
            return botLeftChatProcessor.processLeave(botUpdated);
        }
        return botJoinChatProcessor.updateChatInfo(botUpdated);
    }

    private BotAction processUserUpdated(ChatMemberUpdated userUpdated) {
        if (isLeft(userUpdated) || isKicked(userUpdated)) {
            return userLeftChatProcessor.processLeave(userUpdated);
        }
        return null;
    }

    private boolean isLeft(ChatMemberUpdated memberUpdated) {
       ChatMember newMember = memberUpdated.getNewChatMember();
       return newMember.getStatus().equals("left");
    }

    private boolean isKicked(ChatMemberUpdated memberUpdated) {
        ChatMember newMember = memberUpdated.getNewChatMember();
        return newMember.getStatus().equals("kicked");
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
