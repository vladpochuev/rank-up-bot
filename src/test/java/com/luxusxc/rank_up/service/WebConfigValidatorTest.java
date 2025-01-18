package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebConfigValidatorTest {
    private static WebConfigValidator validator;
    private static WebRankUpConfig defaultConfig;
    private static DefaultRankRepository defaultRankRepository;

    @BeforeEach
    void init() {
        defaultRankRepository = mock();
        StringSplitter splitter = new StringSplitter();
        validator = new WebConfigValidator(defaultRankRepository, splitter);
        defaultConfig = new WebRankUpConfig(true,
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
    void testLevelsExtraComa() {
        defaultConfig.setCustomLevels("123,");
        assertConfigInvalid();
    }

    @Test
    void testLevelsExtraSpaces() {
        defaultConfig.setCustomLevels(" 123  , 2    ");
        assertConfigInvalid();
    }

    @Test
    void testEmptyLevels() {
        defaultConfig.setCustomLevels("");
        assertConfigInvalid();
    }

    @Test
    void testLevelsNull() {
        defaultConfig.setCustomLevels(null);
        assertConfigInvalid();
    }

    @Test
    void testDefaultLevels() {
        List<DefaultRankEntity> defaultRanks = List.of(
                new DefaultRankEntity(1, "LEGEND", 40L),
                new DefaultRankEntity(2, "ANCIENT", 50L),
                new DefaultRankEntity(3, "DIVINE", 60L));
        when(defaultRankRepository.findAll()).thenReturn(defaultRanks);
        defaultConfig.setEnableCustomLevels(false);
        defaultConfig.setCustomLevels(null);
        assertConfigValid();
    }

    @Test
    void testIsRanksExtraLinebreak() {
        defaultConfig.setCustomRanks("TEST1\nTEST2\n");
        assertConfigInvalid();
    }

    @Test
    void testRanksExtraSpaces() {
        defaultConfig.setCustomRanks("TEST1  \n TEST2\n   TEST3");
        assertConfigValid();
    }

    @Test
    void testEmptyRanks() {
        defaultConfig.setCustomRanks("");
        assertConfigInvalid();
    }

    @Test
    void testRanksNull() {
        defaultConfig.setCustomRanks(null);
        assertConfigInvalid();
    }

    @Test
    void testDefaultRanks() {
        List<DefaultRankEntity> defaultRanks = List.of(
                new DefaultRankEntity(1, "LEGEND", 40L),
                new DefaultRankEntity(2, "ANCIENT", 50L),
                new DefaultRankEntity(3, "DIVINE", 60L));
        when(defaultRankRepository.findAll()).thenReturn(defaultRanks);
        defaultConfig.setEnableCustomRanks(false);
        defaultConfig.setCustomRanks(null);
        assertConfigValid();
    }

    @Test
    void testImagesNotUrl() {
        defaultConfig.setAttachedImagesUrl("TEST");
        assertConfigInvalid();
    }

    @Test
    void testImagesExtraSpace() {
        defaultConfig.setAttachedImagesUrl(" https://i.imgur.com/zfjDHmI.jpeg  ");
        assertConfigValid();
    }

    @Test
    void testImagesEmpty() {
        defaultConfig.setAttachedImagesUrl("");
        assertConfigValid();
    }

    @Test
    void testImagesNull() {
        defaultConfig.setAttachedImagesUrl(null);
        assertConfigInvalid();
    }

    @Test
    void testSeveralImages() {
        defaultConfig.setAttachedImagesUrl("https://i.imgur.com/zfjDHmI.jpeg\nhttps://i.imgur.com/ZmcYXQK.jpeg");
        assertConfigValid();
    }

    @Test
    void testMessageNull() {
        defaultConfig.setLevelUpMessage(null);
        assertConfigInvalid();
    }

    @Test
    void testMessageEmpty() {
        defaultConfig.setLevelUpMessage("");
        assertConfigValid();
    }

    @Test
    void testQuantity() {
        defaultConfig.setCustomLevels("1, 2, 3");
        defaultConfig.setCustomRanks("TEST1, TEST2");
        assertConfigInvalid();
    }

    @Test
    void testDefaultQuantity() {
        when(defaultRankRepository.findAll()).thenReturn(List.of(new DefaultRankEntity(1, "TEST", 12L)));
        defaultConfig.setEnableCustomLevels(false);
        defaultConfig.setCustomLevels(null);
        defaultConfig.setEnableCustomRanks(true);
        defaultConfig.setCustomRanks("TEST1\nTEST2");

        assertConfigInvalid();
    }

    @Test
    void testDefaultQuantityLessCustom() {
        List<DefaultRankEntity> defaultRanks = List.of(
                new DefaultRankEntity(1, "TEST1", 11L),
                new DefaultRankEntity(2, "TEST2", 12L));
        when(defaultRankRepository.findAll()).thenReturn(defaultRanks);
        defaultConfig.setEnableCustomLevels(false);
        defaultConfig.setCustomLevels(null);
        defaultConfig.setEnableCustomRanks(true);
        defaultConfig.setCustomRanks("TEST");

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

    private void assertConfigInvalid() {
        boolean isValid = validator.isValid(defaultConfig);
        assertThat(isValid, is(false));
    }
}