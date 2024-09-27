package com.luxusxc.rank_up.model;

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
        rankEntity = new RankEntity(new Rank("HERALD", 1), 10L , "");
        userEntity = new UserEntity(1, "Name", "", "", Timestamp.valueOf(LocalDateTime.now()), rankEntity.getRank(), 1);
    }

    @Test
    void testLvl() {
        String levelStr = Variable.NEW_LEVEL.getCallback().apply(rankEntity);
        assertThat(Integer.valueOf(levelStr), equalTo(rankEntity.getRank().getRankLevel()));
    }

    @Test
    void testRank() {
        String rank = Variable.NEW_RANK.getCallback().apply(rankEntity);
        assertThat(rank, equalTo(rankEntity.getRank().getRankName()));
    }

    @Test
    void testName() {
        String name = Variable.NAME.getCallback().apply(userEntity);
        assertThat(name, equalTo(userEntity.getName()));
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
