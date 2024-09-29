package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatMessageProcessor {
    private final UserRepository userRepository;
    private final RankRepository rankRepository;

    public void processMessage(Message message) {
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();
        Optional<UserEntity> userOptional = userRepository.findById(new ChatUserId(chatId, userId));

        if (userOptional.isPresent()) {
            updateLevel(userOptional.get());
        } else {
            createNewUser(chatId, message.getFrom());
        }
    }

    private void updateLevel(UserEntity userEntity) {
        Integer level = userEntity.getRankLevel();
        Long experience = userEntity.getExperience();
        RankEntity rankEntity = rankRepository.findById(level).orElseThrow();

        if (experience >= rankEntity.getExperience()) {
            if (isMaxLevel(level)) return;
            userEntity.setRankLevel(level + 1);
            userEntity.setExperience(1L);
        } else {
            userEntity.setExperience(experience + 1);
        }

        userRepository.save(userEntity);
    }

    private boolean isMaxLevel(Integer level) {
        return rankRepository.findById(level + 1).isEmpty();
    }

    private void createNewUser(Long chatId, User user) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        UserEntity userEntity = new UserEntity();
        userEntity.setChatUserId(new ChatUserId(chatId, user.getId()));
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setLanguageCode(user.getLanguageCode());
        userEntity.setRegisteredAt(now);
        userEntity.setRankLevel(1);
        userEntity.setExperience(1L);

        userRepository.save(userEntity);
    }
}
