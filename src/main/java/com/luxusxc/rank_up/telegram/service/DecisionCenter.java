package com.luxusxc.rank_up.telegram.service;

import com.luxusxc.rank_up.telegram.config.BotConfig;
import com.luxusxc.rank_up.telegram.model.BotAction;
import com.luxusxc.rank_up.telegram.command.CommandType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.*;
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

    private final ChatMemberStatus status;
    private final BotConfig botConfig;

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
        ChatMember bot = botUpdated.getNewChatMember();
        if (status.isLeft(bot) || status.isKicked(bot)) {
            return botLeftChatProcessor.processLeave(botUpdated);
        }
        return botJoinChatProcessor.updateChatInfo(botUpdated);
    }

    private BotAction processUserUpdated(ChatMemberUpdated userUpdated) {
        ChatMember user = userUpdated.getNewChatMember();
        if (status.isLeft(user) || status.isKicked(user)) {
            return userLeftChatProcessor.processLeave(userUpdated);
        }
        return null;
    }

    private BotAction processMessage(Message message) {
        if (!message.hasText()) return null;

        Chat chat = message.getChat();
        if (chat.isUserChat()) {
            return commandProcessor.processUserCommand(message);
        } else if (isGroupChat(chat) && isGroupCommand(message)) {
            return commandProcessor.processGroupCommand(message);
        } else if (isGroupChat(chat)) {
            return chatMessageProcessor.processMessage(message);
        }
        return null;
    }

    private boolean isGroupChat(Chat chat) {
        return chat.isGroupChat() || chat.isSuperGroupChat();
    }

    private boolean isGroupCommand(Message message) {
        String commandBody = message.getText().trim();
        for (CommandType groupCommand : CommandType.getGroupCommands()) {
            String groupCommandBody = groupCommand.body;
            if (commandBody.equals(groupCommandBody) || commandBody.equals(groupCommandBody + "@" + botConfig.getBotName())) {
                return true;
            }
        }
        return false;
    }

    private BotAction processCallback(CallbackQuery callback) {
        return callbackProcessor.processCallback(callback);
    }
}
