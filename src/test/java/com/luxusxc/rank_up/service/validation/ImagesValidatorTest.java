package com.luxusxc.rank_up.service.validation;

import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.common.service.validation.ImagesValidator;
import com.luxusxc.rank_up.common.service.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ImagesValidatorTest {
    private final Validator validator = new ImagesValidator();

    @Test
    void testImagesNotUrl() {
        WebConfig config = new WebConfig();
        config.setAttachedImagesUrl("TEST");
        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testImagesExtraSpace() {
        WebConfig config = new WebConfig();
        config.setAttachedImagesUrl(" https://i.imgur.com/zfjDHmI.jpeg  ");
        assertThat(validator.isValid(config), is(true));
    }

    @Test
    void testImagesEmpty() {
        WebConfig config = new WebConfig();
        config.setAttachedImagesUrl("");
        assertThat(validator.isValid(config), is(true));
    }

    @Test
    void testImagesNull() {
        WebConfig config = new WebConfig();
        config.setAttachedImagesUrl(null);
        assertThat(validator.isValid(config), is(false));
    }

    @Test
    void testSeveralImages() {
        WebConfig config = new WebConfig();
        config.setAttachedImagesUrl("https://i.imgur.com/zfjDHmI.jpeg\nhttps://i.imgur.com/ZmcYXQK.jpeg");
        assertThat(validator.isValid(config), is(true));
    }
}