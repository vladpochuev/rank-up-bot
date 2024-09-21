package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.Rank;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RanksTest {
    private final Ranks ranks;
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;

    public RanksTest() {
        repository = mock();
        defaultRepository = mock();
        this.ranks = new Ranks(repository, defaultRepository, new StringSplitter(), new StringJoiner());
    }

    @Test
    void testExportRank() {
        List<RankEntity> rankEntities = new ArrayList<>();
        String levelUpMessage = "name: {name}";
        rankEntities.add(new RankEntity(new Rank("TEST1", 1), 10L, levelUpMessage));

        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(ranks.exportRanks(), equalTo("TEST1"));
    }

    @Test
    void testExportRanks() {
        List<RankEntity> rankEntities = new ArrayList<>();
        String levelUpMessage = "name: {name}";
        rankEntities.add(new RankEntity(new Rank("TEST1", 1), 10L, levelUpMessage));
        rankEntities.add(new RankEntity(new Rank("TEST2", 2), 20L, levelUpMessage));
        rankEntities.add(new RankEntity(new Rank("TEST3", 3), 30L, levelUpMessage));

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
        rankEntities.add(new RankEntity(new Rank(null, 1), 10L, "name: {name}"));
        when(repository.findAll()).thenReturn(rankEntities);
        assertThrows(NullPointerException.class, ranks::exportRanks);
    }

    @Test
    void testImportRanksCustom() {
        RankEntity rankEntity = new RankEntity(new Rank("OLD", 1), null, null);
        when(repository.findAll()).thenReturn(List.of(rankEntity));

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomRanks(true);
        webConfig.setCustomRanks("NEW");
        ranks.importRanks(webConfig);

        ArgumentCaptor<List<RankEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());

        List<RankEntity> capturedRankEntity = captor.getValue();
        assertThat(capturedRankEntity.get(0).getRank(), equalTo(new Rank("NEW", 1)));
    }

    @Test
    void testImportRanksDefault() {
        DefaultRankEntity defaultRankEntity = new DefaultRankEntity(new Rank("OLD", 1), null);
        when(repository.findAll()).thenReturn(List.of(new RankEntity(null, null, null)));
        when(defaultRepository.findAll()).thenReturn(List.of(defaultRankEntity));

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomLevels(false);
        webConfig.setCustomRanks("NEW");
        ranks.importRanks(webConfig);

        ArgumentCaptor<List<RankEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());

        RankEntity capturedRankEntity = captor.getValue().get(0);
        assertThat(capturedRankEntity.getRank(), equalTo(defaultRankEntity.getRank()));
    }

    @Test
    void testImportLevelsNull() {
        assertThrows(NullPointerException.class, () -> ranks.importRanks(null));
    }

    @Test
    void testImportLevelsNullField() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setCustomRanks(null);
        webConfig.setEnableCustomRanks(true);
        assertThrows(IllegalArgumentException.class, () -> ranks.importRanks(webConfig));
    }

    @Test
    void testImportLevelsEmpty() {
        RankEntity rankEntity = new RankEntity(null, 10L, null);
        when(repository.findAll()).thenReturn(List.of(rankEntity));

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomRanks(true);
        webConfig.setCustomRanks("");
        assertThrows(IllegalArgumentException.class, () -> ranks.importRanks(webConfig));
    }
}
