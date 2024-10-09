package com.luxusxc.rank_up.mapper;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class UserMapperTest {
    UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    void testToUserEntity() {
        long chatId = -1L;
        long userId = -2L;
        String firstName = "firstname";
        String lastName = "lastname";
        String username = "username";
        String languageCode = "EN";

        User user = new User(userId, firstName, false, lastName, username, languageCode,
                true, true, true,
                true, true);

        UserEntity userEntity = userMapper.toUserEntity(user, chatId);

        assertThat(userEntity.getChatUserId(), equalTo(new ChatUserId(chatId, userId)));
        assertThat(userEntity.getFirstName(), equalTo(firstName));
        assertThat(userEntity.getLastName(), equalTo(lastName));
        assertThat(userEntity.getUserName(), equalTo(username));
        assertThat(userEntity.getLanguageCode(), equalTo(languageCode));
        assertThat(userEntity.getRankLevel(), equalTo(1));
        assertThat(userEntity.getExperience(), equalTo(1L));
    }

    @Test
    void testToUserEntityNull() {
        UserEntity userEntity = userMapper.toUserEntity(null, -1L);
        assertThat(userEntity, nullValue());
    }
}
