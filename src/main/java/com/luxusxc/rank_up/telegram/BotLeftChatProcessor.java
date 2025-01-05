package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.BotAction;
import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.repository.ChatRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

@Service
@AllArgsConstructor
@Slf4j
public class BotLeftChatProcessor {
    private static final String DELETED_CHAT_LOG_TEMPLATE = "Deleted the chat (chatId=%d)";
    private static final String DELETED_USERS_LOG_TEMPLATE = "Deleted all users (chatId=%d)";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.BOT_SERVICE);

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public BotAction processLeave(ChatMemberUpdated memberUpdated) {
        Chat chat = memberUpdated.getChat();
        if (!chat.isGroupChat() && !chat.isSuperGroupChat()) {
            return null;
        }
        return bot -> deleteChat(chat);
    }

    private void deleteChat(Chat chat) {
        long chatId = chat.getId();
        deleteChatEntity(chatId);
        deleteChatUserEntities(chatId);
    }

    private void deleteChatEntity(long chatId) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(chatId);
        chatRepository.delete(chatEntity);
        log.info(LOG_MARKER, DELETED_CHAT_LOG_TEMPLATE.formatted(chatId));
    }

    private void deleteChatUserEntities(long chatId) {
        userRepository.deleteAllChatUsers(chatId);
        log.info(LOG_MARKER, DELETED_USERS_LOG_TEMPLATE.formatted(chatId));
    }
}
