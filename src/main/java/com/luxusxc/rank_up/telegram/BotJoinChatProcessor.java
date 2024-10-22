package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.BotAction;
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
public class BotJoinChatProcessor {
    private final ChatRepository chatRepository;
    private final ChatMemberStatus status;

    public BotAction updateChatInfo(ChatMemberUpdated myChatMember) {
        ChatMember oldMember = myChatMember.getOldChatMember();
        ChatMember newMember = myChatMember.getNewChatMember();
        Chat chat = myChatMember.getChat();

        if (!chat.isGroupChat() && !chat.isSuperGroupChat()) return null;

        return bot -> {
            boolean commonMember = status.isMember(oldMember) || status.isKicked(oldMember) || status.isLeft(oldMember);
            boolean promotedToAdmin = status.isAdmin(newMember);

            if (commonMember && promotedToAdmin) {
                ChatEntity chatEntity = new ChatEntity(chat.getId(), chat.getTitle(), myChatMember.getDate());
                chatRepository.save(chatEntity);
                log.info("Created new chat with id " + chat.getId());
            }
        };
    }
}
