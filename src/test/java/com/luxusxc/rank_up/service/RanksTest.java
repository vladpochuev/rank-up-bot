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

public class RanksTest {
    private final Ranks ranks;
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;

    public RanksTest() {
        repository = mock();
        defaultRepository = mock();
        this.ranks = new Ranks(repository, defaultRepository, new StringSplitter(), new StringJoiner(), new RankEntityFactory());
    }

    @Test
    void testExportRank() {
        List<RankEntity> rankEntities = new ArrayList<>();
        String levelUpMessage = "name: {name}";
        rankEntities.add(new RankEntity(1, "TEST1", 10L, levelUpMessage));

        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(ranks.exportRanks(), equalTo("TEST1"));
    }

    @Test
    void testExportRanks() {
        List<RankEntity> rankEntities = new ArrayList<>();
        String levelUpMessage = "name: {name}";
        rankEntities.add(new RankEntity(1, "TEST1", 10L, levelUpMessage));
        rankEntities.add(new RankEntity(2, "TEST2", 20L, levelUpMessage));
        rankEntities.add(new RankEntity(3, "TEST3", 30L, levelUpMessage));

        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(ranks.exportRanks(), equalTo("TEST1\nTEST2\nTEST3"));
    }

    @Test
    void testExportRanksEmpty() {
        List<RankEntity> rankEntities = new ArrayList<>();
        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(ranks.exportRanks(), equalTo(""));
    }

    @Test
    void testExportRanksNull() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(null);
        when(repository.findAll()).thenReturn(rankEntities);
        assertThrows(NullPointerException.class, ranks::exportRanks);
    }

    @Test
    void testExportRanksNullRank() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(new RankEntity(null, 10L, "name: {name}"));
        when(repository.findAll()).thenReturn(rankEntities);
        assertThrows(NullPointerException.class, ranks::exportRanks);
    }

    @Test
    void testExportRanksNullRankName() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(new RankEntity(1, null, 10L, "name: {name}"));
        when(repository.findAll()).thenReturn(rankEntities);
        assertThrows(NullPointerException.class, ranks::exportRanks);
    }

    @Test
    void testImportRanksCustom() {
        RankEntity rankEntity = new RankEntity(1, "OLD", null, null);

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomRanks(true);
        webConfig.setCustomRanks("NEW");
        ranks.fillRanks(webConfig, List.of(rankEntity));

        assertThat(rankEntity.getName(), equalTo("NEW"));
    }

    @Test
    void testImportRanksDefault() {
        RankEntity rankEntity = new RankEntity(null, null, null);
        DefaultRankEntity defaultRankEntity = new DefaultRankEntity(1, "OLD", null);
        when(defaultRepository.findAll()).thenReturn(List.of(defaultRankEntity));

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomLevels(false);
        webConfig.setCustomRanks("NEW");
        ranks.fillRanks(webConfig, List.of(rankEntity));

        assertThat(rankEntity.getName(), equalTo(defaultRankEntity.getName()));
    }

    @Test
    void testImportLevelsNull() {
        RankEntity rankEntity = new RankEntity(1, "OLD", null, null);
        assertThrows(NullPointerException.class, () -> ranks.fillRanks(null, List.of(rankEntity)));
    }

    @Test
    void testImportLevelsNullField() {
        RankEntity rankEntity = new RankEntity(1, "OLD", null, null);

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setCustomRanks(null);
        webConfig.setEnableCustomRanks(true);
        assertThrows(IllegalArgumentException.class, () -> ranks.fillRanks(webConfig, List.of(rankEntity)));
    }

    @Test
    void testImportLevelsEmpty() {
        RankEntity rankEntity = new RankEntity(1, "OLD", null, null);

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomRanks(true);
        webConfig.setCustomRanks("");
        assertThrows(IllegalArgumentException.class, () -> ranks.fillRanks(webConfig, List.of(rankEntity)));
    }
}
