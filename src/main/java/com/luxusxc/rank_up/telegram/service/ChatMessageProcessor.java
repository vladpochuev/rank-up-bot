package com.luxusxc.rank_up.telegram.service;

import com.luxusxc.rank_up.telegram.mapper.UserMapper;
import com.luxusxc.rank_up.common.model.*;
import com.luxusxc.rank_up.common.repository.RankRepository;
import com.luxusxc.rank_up.common.service.ConfigHandler;
import com.luxusxc.rank_up.telegram.model.BotAction;
import com.luxusxc.rank_up.telegram.model.ChatUserId;
import com.luxusxc.rank_up.telegram.model.UserEntity;
import com.luxusxc.rank_up.telegram.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ChatMessageProcessor {
    private static final String MAX_LEVEL_LOG_TEMPLATE = "Tried to update user's level, but it has already reached the maximum (%s)";
    private static final String LEVEL_UPDATED_LOG_TEMPLATE = "User's level was updated (%s)";
    private static final String NEW_USER_LOG_TEMPLATE = "New user was created (%s)";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.BOT_SERVICE);

    private final UserRepository userRepository;
    private final RankRepository rankRepository;
    private final ConfigHandler configHandler;
    private final UserMapper userMapper;
    private final LevelUpAnnouncer levelUpAnnouncer;

    public BotAction processMessage(Message message) {
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();
        Optional<UserEntity> userOptional = userRepository.findById(new ChatUserId(chatId, userId));

        if (userOptional.isPresent()) {
            return updateLevel(userOptional.get());
        } else {
            return createNewUser(message.getFrom(), chatId);
        }
    }

    private BotAction updateLevel(UserEntity userEntity) {
        return bot -> {
            Integer level = userEntity.getRankLevel();
            Long experience = userEntity.getExperience();
            RankEntity rank = rankRepository.findById(level).orElseThrow();

            if (experience >= rank.getExperience()) {
                if (isMaxLevel(level)) {
                    log.info(LOG_MARKER, MAX_LEVEL_LOG_TEMPLATE.formatted(userEntity.getChatUserId()));
                    return;
                }

                incrementLevel(userEntity);
                announceLevelUpIfEnabled(userEntity, bot);
            } else {
                userEntity.setExperience(experience + 1);
            }

            userRepository.save(userEntity);
            log.info(LOG_MARKER, LEVEL_UPDATED_LOG_TEMPLATE.formatted(userEntity.getChatUserId()));
        };
    }

    private boolean isMaxLevel(Integer level) {
        return rankRepository.findById(level + 1).isEmpty();
    }

    private void incrementLevel(UserEntity userEntity) {
        Integer level = userEntity.getRankLevel();
        userEntity.setRankLevel(level + 1);
        userEntity.setExperience(1L);
    }

    private void announceLevelUpIfEnabled(UserEntity userEntity, TelegramBot bot) {
        Config config = configHandler.getConfig();
        if (config.isAnnounceLevelUp()) {
            levelUpAnnouncer.announce(bot, userEntity);
        }
    }

    private BotAction createNewUser(User user, Long chatId) {
        return bot -> {
            UserEntity userEntity = userMapper.toUserEntity(user, chatId);
            userRepository.save(userEntity);
            log.info(LOG_MARKER, NEW_USER_LOG_TEMPLATE.formatted(userEntity.getChatUserId()));
        };
    }
}
