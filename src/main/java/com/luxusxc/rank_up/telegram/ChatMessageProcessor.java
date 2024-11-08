package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.mapper.UserMapper;
import com.luxusxc.rank_up.model.*;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.service.RankUpConfigHandler;
import com.luxusxc.rank_up.service.VariableReplacer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatMessageProcessor {
    private final UserRepository userRepository;
    private final RankRepository rankRepository;
    private final RankUpConfigHandler configHandler;
    private final VariableReplacer variableReplacer;
    private final UserMapper userMapper;

    public BotAction processMessage(Message message) {
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();
        Optional<UserEntity> userOptional = userRepository.findById(new ChatUserId(chatId, userId));

        BotAction action;
        if (userOptional.isPresent()) {
            action = updateLevel(userOptional.get());
        } else {
            action = createNewUser(message.getFrom(), chatId);
        }
        return action;
    }

    private BotAction updateLevel(UserEntity userEntity) {
        return bot -> {
            Integer level = userEntity.getRankLevel();
            Long experience = userEntity.getExperience();
            RankEntity rank = rankRepository.findById(level).orElseThrow();

            if (experience >= rank.getExperience()) {
                if (isMaxLevel(level)) return;
                increaseLevel(userEntity);
                announceLevelUpIfEnabled(userEntity, bot);
            } else {
                userEntity.setExperience(experience + 1);
            }

            userRepository.save(userEntity);
        };
    }

    private boolean isMaxLevel(Integer level) {
        return rankRepository.findById(level + 1).isEmpty();
    }

    private void increaseLevel(UserEntity userEntity) {
        Integer level = userEntity.getRankLevel();
        userEntity.setRankLevel(level + 1);
        userEntity.setExperience(1L);
    }

    private void announceLevelUpIfEnabled(UserEntity userEntity, TelegramBot bot) {
        RankUpConfig config = configHandler.getConfig();
        if (config.isAnnounceLevelUp()) {
            announceLevelUp(bot, userEntity);
        }
    }

    private void announceLevelUp(TelegramBot bot, UserEntity userEntity) {
        RankEntity newRank = rankRepository.findById(userEntity.getRankLevel()).orElseThrow();
        String message = newRank.getLevelUpMessage();
        String userMessage = variableReplacer.replaceUserVars(message, userEntity);
        long chatId = userEntity.getChatUserId().getChatId();
        bot.sendMessage(chatId, userMessage);
    }

    private BotAction createNewUser(User user, Long chatId) {
        return bot -> {
            UserEntity userEntity = userMapper.toUserEntity(user, chatId);
            userRepository.save(userEntity);
        };
    }
}
