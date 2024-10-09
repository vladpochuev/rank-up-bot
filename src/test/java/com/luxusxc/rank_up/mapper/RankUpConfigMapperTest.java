package com.luxusxc.rank_up.mapper;

import com.luxusxc.rank_up.model.RankUpConfig;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class RankUpConfigMapperTest {
    private final RankUpConfigMapper mapper = RankUpConfigMapper.INSTANCE;
    @Test
    void testCreateFromConfig() {
        RankUpConfig config = new RankUpConfig(false, true, false, true, "name: {name}");
        WebRankUpConfig webConfig = mapper.toWebRankUpConfig(config);
        assertThat(webConfig.isEnableAll(), equalTo(config.isEnableAll()));
        assertThat(webConfig.isEnableCustomRanks(), equalTo(config.isEnableCustomRanks()));
        assertThat(webConfig.isEnableCustomLevels(), equalTo(config.isEnableCustomLevels()));
        assertThat(webConfig.isAnnounceLevelUp(), equalTo(config.isAnnounceLevelUp()));
        assertThat(webConfig.getLevelUpMessage(), equalTo(config.getLevelUpMessageFormat()));
    }

    @Test
    void testCreateFromConfigNull() {
        assertThat(mapper.toWebRankUpConfig(null), nullValue());
    }

    @Test
    void testCreateFromConfigNullFields() {
        RankUpConfig config = new RankUpConfig();
        config.setLevelUpMessageFormat(null);
        WebRankUpConfig webConfig = mapper.toWebRankUpConfig(config);
        assertThat(webConfig.getLevelUpMessage(), nullValue());
    }
}
