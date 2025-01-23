package com.luxusxc.rank_up.service.validation;

import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.common.service.validation.RanksValidator;
import com.luxusxc.rank_up.common.service.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class RanksValidatorTest {
    private final Validator validator = new RanksValidator();
    @Test
    void testRanksExtraLinebreak() {
        WebConfig config = new WebConfig();
        config.setEnableCustomRanks(true);
        config.setCustomRanks("TEST1\nTEST2\n");
        assertThat(validator.isValid(config), is(true));
    }

    @Test
    void testRanksExtraSpaces() {
        WebConfig config = new WebConfig();
        config.setEnableCustomRanks(true);
        config.setCustomRanks("TEST1  \n TEST2\n   TEST3");
        assertThat(validator.isValid(config), is(true));
    }

    @Test
    void testEmptyRanks() {
        WebConfig config = new WebConfig();
        config.setEnableCustomRanks(true);
        config.setCustomRanks("");
        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testRanksNull() {
        WebConfig config = new WebConfig();
        config.setEnableCustomRanks(true);
        config.setCustomRanks(null);
        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testDefaultRanks() {
        WebConfig config = new WebConfig();
        config.setEnableCustomRanks(false);
        config.setCustomRanks(null);
        assertThat(validator.isValid(config), is(true));
    }
}