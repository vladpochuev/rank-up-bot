package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.BotAction;
import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

@Service
@AllArgsConstructor
@Slf4j
public class UserLeftChatProcessor {
    private static final String USER_LEAVE_LOG_TEMPLATE = "User leaved the group (%s)";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.BOT_SERVICE);

    private final UserRepository userRepository;
    public BotAction processLeave(ChatMemberUpdated memberUpdated) {
        return bot -> {
            Long chatId = memberUpdated.getChat().getId();
            Long userId = memberUpdated.getOldChatMember().getUser().getId();

            ChatUserId chatUserId = new ChatUserId(chatId, userId);
            UserEntity userEntity = new UserEntity(chatUserId);

            userRepository.delete(userEntity);
            log.info(LOG_MARKER, USER_LEAVE_LOG_TEMPLATE.formatted(chatUserId));
        };
    }
}
