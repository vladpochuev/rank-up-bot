package com.luxusxc.rank_up.service.validation;

import com.luxusxc.rank_up.web.model.DefaultRankEntity;
import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.web.repository.DefaultRankRepository;
import com.luxusxc.rank_up.common.service.StringSplitter;
import com.luxusxc.rank_up.common.service.validation.QuantityValidator;
import com.luxusxc.rank_up.common.service.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuantityValidatorTest {
    private DefaultRankRepository defaultRankRepository;
    private Validator validator;

    @BeforeEach
    void init() {
        StringSplitter splitter = new StringSplitter();
        defaultRankRepository = mock();
        validator = new QuantityValidator(defaultRankRepository, splitter);

    }

    @Test
    void testQuantity() {
        WebConfig config = new WebConfig();
        config.setEnableCustomLevels(true);
        config.setCustomLevels("1, 2, 3");
        config.setEnableCustomRanks(true);
        config.setCustomRanks("TEST1\nTEST2");
        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testDefaultQuantity() {
        when(defaultRankRepository.findAll()).thenReturn(List.of(new DefaultRankEntity(1, "TEST", 12L)));

        WebConfig config = new WebConfig();
        config.setEnableCustomLevels(false);
        config.setCustomLevels(null);
        config.setEnableCustomRanks(true);
        config.setCustomRanks("TEST1\nTEST2");

        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testDefaultQuantityLessCustom() {
        List<DefaultRankEntity> defaultRanks = List.of(
                new DefaultRankEntity(1, "TEST1", 11L),
                new DefaultRankEntity(2, "TEST2", 12L));
        when(defaultRankRepository.findAll()).thenReturn(defaultRanks);

        WebConfig config = new WebConfig();
        config.setEnableCustomLevels(false);
        config.setCustomLevels(null);
        config.setEnableCustomRanks(true);
        config.setCustomRanks("TEST");

        assertThat(validator.isValid(config), is(true));
    }
}