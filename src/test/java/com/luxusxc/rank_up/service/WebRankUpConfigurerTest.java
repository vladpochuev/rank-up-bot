package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.mapper.DefaultRankEntityMapper;
import com.luxusxc.rank_up.mapper.RankUpConfigMapper;
import com.luxusxc.rank_up.model.*;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.ImageRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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

        Ranks ranks = new Ranks(rankRepository, defaultRankRepository, splitter, joiner, DefaultRankEntityMapper.INSTANCE);
        Levels levels = new Levels(rankRepository, defaultRankRepository, splitter, joiner, DefaultRankEntityMapper.INSTANCE);
        Images images = new Images(imageRepository, splitter, joiner);
        LevelUpMessages messages = new LevelUpMessages(new VariableReplacer());
        configurer = new WebRankUpConfigurer(rankRepository, defaultRankRepository, ranks, levels, images, messages, configHandler, RankUpConfigMapper.INSTANCE, splitter);
    }

    @BeforeEach
    void init() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(new RankEntity(1, "HERALD", 10L, ""));
        rankEntities.add(new RankEntity(2, "GUARDIAN", 20L, ""));
        rankEntities.add(new RankEntity(3, "CRUSADER", 30L, ""));

        List<DefaultRankEntity> defaultRankEntities = new ArrayList<>();
        defaultRankEntities.add(new DefaultRankEntity(1, "ARCHON", 20L));
        defaultRankEntities.add(new DefaultRankEntity(2, "LEGEND", 40L));
        defaultRankEntities.add(new DefaultRankEntity(3, "ANCIENT", 60L));

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
        RankEntity expected = new RankEntity(1, "ANCIENT", 20L, "1: ANCIENT");
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
        RankEntity expected = new RankEntity(1, "ARCHON", 5L, "1: ARCHON");
        assertThat(capturedEntities, equalTo(expected));
    }

    @Test
    void testImportWebConfig3() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage("{newlvl}: {newrank}");
        webConfig.setEnableCustomLevels(true);
        webConfig.setCustomLevels("12, 13, 14");
        webConfig.setCustomRanks("HERALD\nGUARDIAN\nCRUSADERS");
        webConfig.setAttachedImagesUrl("");

        configurer.importWebConfig(webConfig);

        ArgumentCaptor<List<RankEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(rankRepository).saveAll(captor.capture());

        RankEntity capturedEntities = captor.getValue().get(0);
        RankEntity expected = new RankEntity(1, "ARCHON", 12L, "1: ARCHON");
        assertThat(capturedEntities, equalTo(expected));
    }
}