package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Service
@Slf4j
@AllArgsConstructor
public class JoinChatHandler {
    private final ChatRepository chatRepository;

    public void updateChatInfo(ChatMemberUpdated myChatMember) {
        ChatMember oldMember = myChatMember.getOldChatMember();
        ChatMember newMember = myChatMember.getNewChatMember();
        Chat chat = myChatMember.getChat();

        if (!chat.isGroupChat() && !chat.isSuperGroupChat()) return;

        if ((isMember(oldMember) || isKicked(oldMember)) && isAdmin(newMember)) {
            ChatEntity chatEntity = new ChatEntity(chat.getId(), chat.getTitle(), myChatMember.getDate());
            chatRepository.save(chatEntity);
            log.info("Created new chat with id " + chat.getId());
        }
    }

    private boolean isMember(ChatMember chatMember) {
        String status = chatMember.getStatus();
        return status.equals("member");
    }

    private boolean isKicked(ChatMember chatMember) {
        String status = chatMember.getStatus();
        return status.equals("kicked");
    }

    private boolean isAdmin(ChatMember chatMember) {
        String status = chatMember.getStatus();
        return status.equals("administrator");
    }
}
