package com.luxusxc.rank_up.mapper;

import com.luxusxc.rank_up.web.mapper.WebConfigMapper;
import com.luxusxc.rank_up.common.model.Config;
import com.luxusxc.rank_up.web.model.WebConfig;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class WebConfigMapperTest {
    private final WebConfigMapper mapper = WebConfigMapper.INSTANCE;

    @Test
    void testToRankUpConfig() {
        WebConfig webConfig = new WebConfig(false, true, "10, 20",
                "LEGEND\nANCIENT", false, "HEROLD\nCRUSADER", "30, 40", true,
                "name: {name}", "");
        Config config = mapper.toRankUpConfig(webConfig);

        assertThat(config.isEnableAll(), equalTo(webConfig.isEnableAll()));
        assertThat(config.isEnableCustomRanks(), equalTo(webConfig.isEnableCustomRanks()));
        assertThat(config.isEnableCustomLevels(), equalTo(webConfig.isEnableCustomLevels()));
        assertThat(config.isAnnounceLevelUp(), equalTo(webConfig.isAnnounceLevelUp()));
        assertThat(config.getLevelUpMessageFormat(), equalTo(webConfig.getLevelUpMessage()));
    }

    @Test
    void testToRankUpConfigNull() {
        Config config = mapper.toRankUpConfig(null);
        assertThat(config, nullValue());
    }

    @Test
    void testToRankUpConfigNullFields() {
        WebConfig webConfig = new WebConfig();
        webConfig.setLevelUpMessage(null);
        Config config = mapper.toRankUpConfig(webConfig);

        assertThat(config.getLevelUpMessageFormat(), nullValue());
    }
}
