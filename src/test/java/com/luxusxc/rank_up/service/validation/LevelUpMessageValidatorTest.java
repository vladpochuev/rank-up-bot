package com.luxusxc.rank_up.service.validation;

import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.common.service.validation.LevelUpMessageValidator;
import com.luxusxc.rank_up.common.service.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LevelUpMessageValidatorTest {
    private final Validator validator = new LevelUpMessageValidator();

    @Test
    void testMessageNull() {
        WebConfig config = new WebConfig();
        config.setLevelUpMessage(null);
        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testMessageEmpty() {
        WebConfig config = new WebConfig();
        config.setLevelUpMessage("");
        assertThat(validator.isValid(config), is(true));
    }
}