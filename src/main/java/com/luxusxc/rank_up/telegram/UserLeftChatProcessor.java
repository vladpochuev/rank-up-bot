package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

@Service
@AllArgsConstructor
public class UserLeftChatProcessor {
    private final UserRepository userRepository;
    public BotAction processLeave(ChatMemberUpdated memberUpdated) {
        return bot -> {
            Long chatId = memberUpdated.getChat().getId();
            Long userId = memberUpdated.getOldChatMember().getUser().getId();

            ChatUserId chatUserId = new ChatUserId(chatId, userId);
            UserEntity userEntity = new UserEntity(chatUserId);

            userRepository.delete(userEntity);
        };
    }
}
