package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.*;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.ImageRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WebRankUpConfigurerTest {
    private final RankRepository rankRepository;
    private final DefaultRankRepository defaultRankRepository;
    private final ImageRepository imageRepository;
    private final RankUpConfigHandler configHandler;
    private final WebRankUpConfigurer configurer;

    public WebRankUpConfigurerTest() {
        rankRepository = mock();
        defaultRankRepository = mock();
        imageRepository = mock();
        configHandler = mock();


        StringSplitter splitter = new StringSplitter();
        StringJoiner joiner = new StringJoiner();
        RankEntityFactory rankFactory = new RankEntityFactory();

        Ranks ranks = new Ranks(rankRepository, defaultRankRepository, splitter, joiner, rankFactory);
        Levels levels = new Levels(rankRepository, defaultRankRepository, splitter, joiner);
        Images images = new Images(imageRepository, splitter, joiner);
        LevelUpMessages messages = new LevelUpMessages(new VariableReplacer());
        configurer = new WebRankUpConfigurer(rankRepository, defaultRankRepository, rankFactory, ranks, levels, images, messages, configHandler);
    }

    @BeforeEach
    void init() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(new RankEntity(new Rank("HERALD", 1), 10L, ""));
        rankEntities.add(new RankEntity(new Rank("GUARDIAN", 2), 20L, ""));
        rankEntities.add(new RankEntity(new Rank("CRUSADER", 3), 30L, ""));

        List<DefaultRankEntity> defaultRankEntities = new ArrayList<>();
        defaultRankEntities.add(new DefaultRankEntity(new Rank("ARCHON", 1), 20L));
        defaultRankEntities.add(new DefaultRankEntity(new Rank("LEGEND", 2), 40L));
        defaultRankEntities.add(new DefaultRankEntity(new Rank("ANCIENT", 3), 60L));

        List<ImageEntity> imageEntities = new ArrayList<>();
        imageEntities.add(new ImageEntity("https://imgur.com/6XexEbp"));
        imageEntities.add(new ImageEntity("https://imgur.com/c9uOWpS"));

        when(rankRepository.findAll()).thenReturn(rankEntities);
        when(defaultRankRepository.findAll()).thenReturn(defaultRankEntities);
        when(imageRepository.findAll()).thenReturn(imageEntities);
    }

    @Test
    void testExportWebConfigRanks() {
        when(configHandler.getConfig()).thenReturn(new RankUpConfig());
        WebRankUpConfig webConfig = configurer.exportWebConfig();
        assertThat(webConfig.getCustomRanks(), equalTo("HERALD\nGUARDIAN\nCRUSADER"));
    }

    @Test
    void testExportWebConfigLevels() {
        when(configHandler.getConfig()).thenReturn(new RankUpConfig());
        WebRankUpConfig webConfig = configurer.exportWebConfig();
        assertThat(webConfig.getCustomRanks(), equalTo("HERALD\nGUARDIAN\nCRUSADER"));
    }

    @Test
    void testExportWebConfigImages() {
        when(configHandler.getConfig()).thenReturn(new RankUpConfig());
        WebRankUpConfig webConfig = configurer.exportWebConfig();
        assertThat(webConfig.getAttachedImagesUrl(), equalTo("https://imgur.com/6XexEbp\nhttps://imgur.com/c9uOWpS"));
    }

    @Test
    void testExportWebConfigMessageFormat() {
        String messageFormat = "name: {name}";
        RankUpConfig config = new RankUpConfig();
        config.setLevelUpMessageFormat(messageFormat);
        when(configHandler.getConfig()).thenReturn(config);
        WebRankUpConfig webConfig = configurer.exportWebConfig();
        assertThat(webConfig.getLevelUpMessage(), equalTo(messageFormat));
    }

    @Test
    void testImportWebConfig() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomRanks(true);
        webConfig.setCustomRanks("ANCIENT\nDIVINE\nIMMORTAL");
        webConfig.setLevelUpMessage("{newlvl}: {newrank}");
        webConfig.setAttachedImagesUrl("");

        configurer.importWebConfig(webConfig);

        ArgumentCaptor<List<RankEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(rankRepository).saveAll(captor.capture());

        RankEntity capturedEntities = captor.getValue().get(0);
        RankEntity expected = new RankEntity(new Rank("ANCIENT", 1), 20L, "1: ANCIENT");
        assertThat(capturedEntities, equalTo(expected));
    }

    @Test
    void testImportWebConfig2() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage("{newlvl}: {newrank}");
        webConfig.setEnableCustomLevels(true);
        webConfig.setCustomLevels("5, 15, 20");
        webConfig.setAttachedImagesUrl("");

        configurer.importWebConfig(webConfig);

        ArgumentCaptor<List<RankEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(rankRepository).saveAll(captor.capture());

        RankEntity capturedEntities = captor.getValue().get(0);
        RankEntity expected = new RankEntity(new Rank("ARCHON", 1), 5L, "1: ARCHON");
        assertThat(capturedEntities, equalTo(expected));
    }

    @Test
    void testImportWebConfig3() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage("{newlvl}: {newrank}");
        webConfig.setEnableCustomLevels(true);
        webConfig.setCustomLevels("12, 13, 14");
        webConfig.setCustomRanks("HERALD, GUARDIAN, CRUSADERS");
        webConfig.setAttachedImagesUrl("");

        configurer.importWebConfig(webConfig);

        ArgumentCaptor<List<RankEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(rankRepository).saveAll(captor.capture());

        RankEntity capturedEntities = captor.getValue().get(0);
        RankEntity expected = new RankEntity(new Rank("ARCHON", 1), 12L, "1: ARCHON");
        assertThat(capturedEntities, equalTo(expected));
    }
}