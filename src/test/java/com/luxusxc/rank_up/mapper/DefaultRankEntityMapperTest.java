package com.luxusxc.rank_up.mapper;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.RankEntity;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class DefaultRankEntityMapperTest {
    private final DefaultRankEntityMapper mapper = DefaultRankEntityMapper.INSTANCE;

    @Test
    void testToRankEntity() {
        DefaultRankEntity defaultRankEntity = new DefaultRankEntity(1, "Herald", 10L);
        RankEntity rankEntity = mapper.toRankEntity(defaultRankEntity);

        assertThat(defaultRankEntity.getLevel(), equalTo(rankEntity.getLevel()));
        assertThat(defaultRankEntity.getName(), equalTo(rankEntity.getName()));
        assertThat(defaultRankEntity.getExperience(), equalTo(rankEntity.getExperience()));
        assertThat(rankEntity.getLevelUpMessage(), nullValue());
    }

    @Test
    void testToRankEntityNull() {
        RankEntity rankEntity = mapper.toRankEntity(null);
        assertThat(rankEntity, nullValue());
    }

    @Test
    void testToRankEntityNullValue() {
        DefaultRankEntity defaultRankEntity = new DefaultRankEntity(1, null, 10L);
        RankEntity rankEntity = mapper.toRankEntity(defaultRankEntity);
        assertThat(rankEntity.getName(), nullValue());
    }
}
