package com.luxusxc.rank_up.mapper;

import com.luxusxc.rank_up.web.mapper.ConfigMapper;
import com.luxusxc.rank_up.common.model.Config;
import com.luxusxc.rank_up.web.model.WebConfig;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class ConfigMapperTest {
    private final ConfigMapper mapper = ConfigMapper.INSTANCE;
    @Test
    void testToWebRankUpConfig() {
        Config config = new Config(false, true, false, true, "name: {name}");
        WebConfig webConfig = mapper.toWebRankUpConfig(config);

        assertThat(webConfig.isEnableAll(), equalTo(config.isEnableAll()));
        assertThat(webConfig.isEnableCustomRanks(), equalTo(config.isEnableCustomRanks()));
        assertThat(webConfig.isEnableCustomLevels(), equalTo(config.isEnableCustomLevels()));
        assertThat(webConfig.isAnnounceLevelUp(), equalTo(config.isAnnounceLevelUp()));
        assertThat(webConfig.getLevelUpMessage(), equalTo(config.getLevelUpMessageFormat()));
    }

    @Test
    void testToWebRankUpConfigNull() {
        WebConfig webConfig = mapper.toWebRankUpConfig(null);
        assertThat(webConfig, nullValue());
    }

    @Test
    void testToWebRankUpConfigNullFields() {
        Config config = new Config();
        config.setLevelUpMessageFormat(null);
        WebConfig webConfig = mapper.toWebRankUpConfig(config);

        assertThat(webConfig.getLevelUpMessage(), nullValue());
    }
}
