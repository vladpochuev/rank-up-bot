package com.luxusxc.rank_up.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private ChatUserId chatUserId;
    private String firstName;
    private String lastName;
    private String username;
    private String languageCode;
    private Timestamp registeredAt;
    private Integer rankLevel;
    private Long experience;

    public UserEntity(ChatUserId chatUserId) {
        this.chatUserId = chatUserId;
    }

    public static UserEntity createFrom(User user, Long chatId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setChatUserId(new ChatUserId(chatId, user.getId()));
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setLanguageCode(user.getLanguageCode());
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        userEntity.setRegisteredAt(now);
        userEntity.setRankLevel(1);
        userEntity.setExperience(1L);

        return userEntity;
    }
}
