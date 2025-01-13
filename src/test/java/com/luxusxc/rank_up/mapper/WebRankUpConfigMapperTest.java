package com.luxusxc.rank_up.mapper;

import com.luxusxc.rank_up.model.RankUpConfig;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class WebRankUpConfigMapperTest {
    private final WebRankUpConfigMapper mapper = WebRankUpConfigMapper.INSTANCE;

    @Test
    void testToRankUpConfig() {
        WebRankUpConfig webConfig = new WebRankUpConfig(false, true, "10, 20",
                "LEGEND\nANCIENT", false, "HEROLD\nCRUSADER", "30, 40", true,
                "name: {name}", "");
        RankUpConfig config = mapper.toRankUpConfig(webConfig);

        assertThat(config.isEnableAll(), equalTo(webConfig.isEnableAll()));
        assertThat(config.isEnableCustomRanks(), equalTo(webConfig.isEnableCustomRanks()));
        assertThat(config.isEnableCustomLevels(), equalTo(webConfig.isEnableCustomLevels()));
        assertThat(config.isAnnounceLevelUp(), equalTo(webConfig.isAnnounceLevelUp()));
        assertThat(config.getLevelUpMessageFormat(), equalTo(webConfig.getLevelUpMessage()));
    }

    @Test
    void testToRankUpConfigNull() {
        RankUpConfig config = mapper.toRankUpConfig(null);
        assertThat(config, nullValue());
    }

    @Test
    void testToRankUpConfigNullFields() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage(null);
        RankUpConfig config = mapper.toRankUpConfig(webConfig);

        assertThat(config.getLevelUpMessageFormat(), nullValue());
    }
}
