package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LevelsTest {
    private final Levels levels;
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;

    public LevelsTest() {
        repository = mock();
        defaultRepository = mock();
        this.levels = new Levels(repository, defaultRepository, new StringSplitter(), new StringJoiner());
    }

    @Test
    void testExportLevel() {
        List<RankEntity> rankEntities = new ArrayList<>();
        String levelUpMessage = "name: {name}";
        rankEntities.add(new RankEntity(null, 10L, levelUpMessage));

        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(levels.exportLevels(), equalTo("10"));
    }

    @Test
    void testExportLevels() {
        List<RankEntity> rankEntities = new ArrayList<>();
        String levelUpMessage = "name: {name}";
        rankEntities.add(new RankEntity(null, 10L, levelUpMessage));
        rankEntities.add(new RankEntity(null, 20L, levelUpMessage));
        rankEntities.add(new RankEntity(null, 30L, levelUpMessage));

        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(levels.exportLevels(), equalTo("10, 20, 30"));
    }

    @Test
    void testExportLevelsEmpty() {
        List<RankEntity> rankEntities = new ArrayList<>();
        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(levels.exportLevels(), equalTo(""));
    }

    @Test
    void testExportLevelsNull() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(null);
        when(repository.findAll()).thenReturn(rankEntities);
        assertThrows(NullPointerException.class, levels::exportLevels);
    }

    @Test
    void testExportLevelsNullField() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(new RankEntity(null, null, "name: {name}"));
        when(repository.findAll()).thenReturn(rankEntities);
        assertThrows(NullPointerException.class, levels::exportLevels);
    }

    @Test
    void testImportLevelsCustom() {
        RankEntity rankEntity = new RankEntity(null, 10L, null);
        when(repository.findAll()).thenReturn(List.of(rankEntity));

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomLevels(true);
        webConfig.setCustomLevels("20");
        levels.fillLevels(webConfig, List.of(rankEntity));

        assertThat(rankEntity.getExperience(), equalTo(20L));
    }

    @Test
    void testImportLevelsDefault() {
        RankEntity rankEntity =  new RankEntity(null, null, null);
        DefaultRankEntity defaultRankEntity = new DefaultRankEntity(null, 10L);
        when(defaultRepository.findAll()).thenReturn(List.of(defaultRankEntity));

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomLevels(false);
        webConfig.setCustomLevels("20");
        levels.fillLevels(webConfig, List.of(rankEntity));

        assertThat(rankEntity.getExperience(), equalTo(defaultRankEntity.getExperience()));
    }

    @Test
    void testImportLevelsIllegalArgument() {
        RankEntity rankEntity = new RankEntity(null, 10L, null);

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomLevels(true);
        webConfig.setCustomLevels("test");
        assertThrows(IllegalArgumentException.class, () -> levels.fillLevels(webConfig, List.of(rankEntity)));
    }

    @Test
    void testImportLevelsNull() {
        RankEntity rankEntity = new RankEntity(null, 10L, null);
        assertThrows(NullPointerException.class, () -> levels.fillLevels(null, List.of(rankEntity)));
    }

    @Test
    void testImportLevelsNullField() {
        RankEntity rankEntity = new RankEntity(null, 10L, null);

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setCustomLevels(null);
        webConfig.setEnableCustomLevels(true);
        assertThrows(IllegalArgumentException.class, () -> levels.fillLevels(webConfig, List.of(rankEntity)));
    }

    @Test
    void testImportLevelsEmpty() {
        RankEntity rankEntity = new RankEntity(null, 10L, null);

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomLevels(true);
        webConfig.setCustomLevels("");
        assertThrows(IllegalArgumentException.class, () -> levels.fillLevels(webConfig, List.of(rankEntity)));
    }
}
