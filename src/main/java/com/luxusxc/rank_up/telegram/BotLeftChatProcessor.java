package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.BotAction;
import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

@Service
@AllArgsConstructor
public class BotLeftChatProcessor {
    private final ChatRepository chatRepository;

    public BotAction processLeave(ChatMemberUpdated memberUpdated) {
        Chat chat = memberUpdated.getChat();
        if (!chat.isGroupChat() && !chat.isSuperGroupChat()) return null;

        return bot -> {
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setId(chat.getId());
            chatRepository.delete(chatEntity);
        };
    }
}
