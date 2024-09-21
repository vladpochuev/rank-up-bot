package com.luxusxc.rank_up.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WebRankUpConfigTest {
    @Test
    void testCreateFromConfig() {
        RankUpConfig config = new RankUpConfig(false, true, false, true, "name: {name}");
        WebRankUpConfig webConfig = WebRankUpConfig.createFrom(config);
        assertThat(webConfig.isEnableAll(), equalTo(config.isEnableAll()));
        assertThat(webConfig.isEnableCustomRanks(), equalTo(config.isEnableCustomRanks()));
        assertThat(webConfig.isEnableCustomLevels(), equalTo(config.isEnableCustomLevels()));
        assertThat(webConfig.isAnnounceLevelUp(), equalTo(config.isAnnounceLevelUp()));
        assertThat(webConfig.getLevelUpMessage(), equalTo(config.getLevelUpMessageFormat()));
    }

    @Test
    void testCreateFromConfigNull() {
        assertThrows(NullPointerException.class, () -> WebRankUpConfig.createFrom(null));
    }

    @Test
    void testCreateFromConfigNullFields() {
        RankUpConfig config = new RankUpConfig();
        config.setLevelUpMessageFormat(null);
        WebRankUpConfig webConfig = WebRankUpConfig.createFrom(config);
        assertThat(webConfig.getLevelUpMessage(), nullValue());
    }
}
