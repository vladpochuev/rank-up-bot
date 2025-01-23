package com.luxusxc.rank_up.model;

import com.luxusxc.rank_up.telegram.model.ChatUserId;
import com.luxusxc.rank_up.common.model.RankEntity;
import com.luxusxc.rank_up.telegram.model.UserEntity;
import com.luxusxc.rank_up.common.service.Variable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VariableTest {
    private RankEntity rankEntity;
    private UserEntity userEntity;
    @BeforeEach
    void init() {
        rankEntity = new RankEntity(1, "HERALD", 10L , "");
        userEntity = new UserEntity(new ChatUserId(1, 1), "Name", "", "", "", Timestamp.valueOf(LocalDateTime.now()), rankEntity.getLevel(), 1L);
    }

    @Test
    void testLvl() {
        String levelStr = Variable.NEW_LEVEL.getCallback().apply(rankEntity);
        assertThat(Integer.valueOf(levelStr), equalTo(rankEntity.getLevel()));
    }

    @Test
    void testRank() {
        String rank = Variable.NEW_RANK.getCallback().apply(rankEntity);
        assertThat(rank, equalTo(rankEntity.getName()));
    }

    @Test
    void testName() {
        String name = Variable.NAME.getCallback().apply(userEntity);
        assertThat(name, equalTo(userEntity.getFirstName()));
    }

    @Test
    void testGetTag() throws NoSuchFieldException, IllegalAccessException {
        Variable variable = Variable.NAME;

        Field tagField = Variable.class.getDeclaredField("tag");
        tagField.setAccessible(true);
        String tag = (String) tagField.get(variable);
        tagField.set(variable, "name");

        assertThat(variable.getTag(), equalTo("name"));

        tagField.set(variable, tag);
        tagField.setAccessible(false);
    }

    @Test
    void testGetTagBracket() {
        String tag = Variable.NAME.getTag();
        assertThat(tag, equalTo("\\{name}"));
    }
}
