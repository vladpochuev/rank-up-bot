package com.luxusxc.rank_up.service.validation;

import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.common.service.validation.LevelsValidator;
import com.luxusxc.rank_up.common.service.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LevelsValidatorTest {
    private final Validator validator = new LevelsValidator();

    @Test
    void testLevelsExtraComa() {
        WebConfig config = new WebConfig();
        config.setEnableCustomLevels(true);
        config.setCustomLevels("123,");
        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testLevelsExtraSpaces() {
        WebConfig config = new WebConfig();
        config.setEnableCustomLevels(true);
        config.setCustomLevels(" 123  , 2    ");
        assertThat(validator.isValid(config), is(true));
    }

    @Test
    void testEmptyLevels() {
        WebConfig config = new WebConfig();
        config.setEnableCustomLevels(true);
        config.setCustomLevels("");
        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testLevelsNull() {
        WebConfig config = new WebConfig();
        config.setEnableCustomLevels(true);
        config.setCustomLevels(null);
        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testDefaultLevels() {
        WebConfig config = new WebConfig();
        config.setEnableCustomLevels(false);
        config.setCustomLevels(null);
        assertThat(validator.isValid(config), is(true));
    }
}