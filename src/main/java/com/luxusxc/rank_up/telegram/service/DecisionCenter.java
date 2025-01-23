package com.luxusxc.rank_up.telegram.service;

import com.luxusxc.rank_up.common.model.LogTags;
import com.luxusxc.rank_up.common.service.ConfigHandler;
import com.luxusxc.rank_up.telegram.command.CommandType;
import com.luxusxc.rank_up.telegram.model.BotAction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Service
@Slf4j
@AllArgsConstructor
public class DecisionCenter {
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.BOT_SERVICE);
    private static final String LEVEL_UP_DISABLED_LOG = "Message wasn't processed because \"enableAll\" option is disabled";

    private final BotJoinChatProcessor botJoinChatProcessor;
    private final BotLeftChatProcessor botLeftChatProcessor;
    private final UserLeftChatProcessor userLeftChatProcessor;
    private final ChatMessageProcessor chatMessageProcessor;
    private final CommandProcessor commandProcessor;
    private final CallbackProcessor callbackProcessor;

    private final ChatMemberStatus status;
    private final ConfigHandler configHandler;
    private final CommandParser commandParser;

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
            return processGroupMessage(message);
        }
        return null;
    }

    private boolean isGroupChat(Chat chat) {
        return chat.isGroupChat() || chat.isSuperGroupChat();
    }

    private boolean isGroupCommand(Message message) {
        String command = message.getText().trim();
        for (CommandType groupCommand : CommandType.getGroupCommands()) {
            String commonCommandBody = commandParser.getCommandBody(command);
            if (commonCommandBody.equals(groupCommand.body)) {
                return true;
            }
        }
        return false;
    }

    private BotAction processGroupMessage(Message message) {
        if (!configHandler.getConfig().isEnableAll()) {
            log.info(LOG_MARKER, LEVEL_UP_DISABLED_LOG);
            return null;
        }
        return chatMessageProcessor.processMessage(message);
    }

    private BotAction processCallback(CallbackQuery callback) {
        return callbackProcessor.processCallback(callback);
    }
}
