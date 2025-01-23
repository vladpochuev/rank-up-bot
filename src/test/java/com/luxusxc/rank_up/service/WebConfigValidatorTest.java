package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.web.repository.DefaultRankRepository;
import com.luxusxc.rank_up.common.service.StringSplitter;
import com.luxusxc.rank_up.common.service.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

class WebConfigValidatorTest {
    private static WebConfigValidator validator;
    private static WebConfig defaultConfig;
    private static DefaultRankRepository defaultRankRepository;

    @BeforeEach
    void init() {
        defaultRankRepository = mock();
        StringSplitter splitter = new StringSplitter();
        validator = new WebConfigValidator(List.of(new LevelsValidator(), new RanksValidator(),
                new ImagesValidator(), new LevelUpMessageValidator(), new QuantityValidator(defaultRankRepository, splitter)));
        defaultConfig = new WebConfig(true,
                true, "10, 20, 30", null,
                true, "HERALD\nGUARDIAN\nCRUSADER", null,
                true, "Congratulations, {name}. You have earned level {newlvl} - {newrank}",
                "https://i.imgur.com/zfjDHmI.jpeg");
    }

    @Test
    void testDefault() {
        assertConfigValid();
    }

    @Test
    void testDefaultAll() {
        defaultConfig.setEnableCustomLevels(false);
        defaultConfig.setCustomLevels(null);
        defaultConfig.setEnableCustomRanks(false);
        defaultConfig.setCustomRanks(null);
        assertConfigValid();
    }

    private void assertConfigValid() {
        boolean isValid = validator.isValid(defaultConfig);
        assertThat(isValid, is(true));
    }
}