package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.BotAction;
import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

@Service
@AllArgsConstructor
public class BotLeftChatProcessor {
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
        deleteChat(chatId);
        deleteChatUsers(chatId);
    }

    private void deleteChat(long chatId) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(chatId);
        chatRepository.delete(chatEntity);
    }

    private void deleteChatUsers(long chatId) {
        userRepository.deleteAllChatUsers(chatId);
    }
}
