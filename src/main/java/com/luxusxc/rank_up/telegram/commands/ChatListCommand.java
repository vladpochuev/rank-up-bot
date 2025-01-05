package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.telegram.InlineKeyboardConstructor;
import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ChatListCommand extends Command {
    private static final String MESSAGE_TEXT = "Choose the chat.";
    private static final String NO_CHATS_TEXT = "You have no rank in any chat.";
    private static final String CALLBACK_PREFIX = "CHAT_";
    private static final String LOG_TEMPLATE = "Chatlist command was called (id=%d)";
    private static final String NO_CHATS_LOG_TEMPLATE = "User has no active chats (id=%d)";
    private static final String SUCCESS_LOG_TEMPLATE = "User has received the chat list (id=%d)";

    public ChatListCommand(TelegramBot bot) {
        super(bot);
    }

    @Override
    public void executeCommand(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        log.info(LOG_MARKER, LOG_TEMPLATE.formatted(chatId));
        List<UserEntity> userProfiles = getUserProfiles(userId);
        if (userProfiles.isEmpty()) {
            sendNoChatMessage(chatId);
        } else {
            List<ChatEntity> userChats = getUserChats(userProfiles);
            sendUserChats(chatId, userChats);
        }
    }

    private List<UserEntity> getUserProfiles(long userId) {
        UserRepository userRepository = bot.getUserRepository();
        return (List<UserEntity>) userRepository.findAll(userId);
    }

    private void sendNoChatMessage(long chatId) {
        bot.sendMessage(chatId, NO_CHATS_TEXT);
        log.info(LOG_MARKER, NO_CHATS_LOG_TEMPLATE.formatted(chatId));
    }

    private List<ChatEntity> getUserChats(List<UserEntity> userProfiles) {
        ChatRepository chatRepository = bot.getChatRepository();

        List<ChatEntity> chats = new ArrayList<>();
        for (UserEntity userProfile : userProfiles) {
            long chatId = userProfile.getChatUserId().getChatId();
            ChatEntity chat = chatRepository.findById(chatId).orElseThrow();
            chats.add(chat);
        }
        return chats;
    }

    private void sendUserChats(long chatId, List<ChatEntity> userChats) {
        InlineKeyboardMarkup markup = getMarkupWithChats(userChats);
        sendSuccessMessage(chatId, markup);
    }

    private InlineKeyboardMarkup getMarkupWithChats(List<ChatEntity> userChats) {
        InlineKeyboardConstructor constructor = bot.getKeyboardConstructor();

        List<Map<String, String>> rows = new ArrayList<>();
        for (ChatEntity chat : userChats) {
            String chatTitle = chat.getTitle();
            rows.add(Map.of(CALLBACK_PREFIX + chat.getId(), chatTitle));
        }
        return constructor.getInlineKeyboard(rows);
    }

    private void sendSuccessMessage(long chatId, InlineKeyboardMarkup markup) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(ChatListCommand.MESSAGE_TEXT);
        response.setReplyMarkup(markup);

        bot.sendMessage(response);
        log.info(LOG_MARKER, SUCCESS_LOG_TEMPLATE.formatted(chatId));
    }
}
