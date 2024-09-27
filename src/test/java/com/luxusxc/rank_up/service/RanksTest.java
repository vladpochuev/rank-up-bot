package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.Rank;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
        this.ranks = new Ranks(repository, defaultRepository, new StringSplitter(), new StringJoiner(), new RankEntityFactory());
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

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomRanks(true);
        webConfig.setCustomRanks("NEW");
        ranks.fillRanks(webConfig, List.of(rankEntity));

        assertThat(rankEntity.getRank(), equalTo(new Rank("NEW", 1)));
    }

    @Test
    void testImportRanksDefault() {
        RankEntity rankEntity = new RankEntity(null, null, null);
        DefaultRankEntity defaultRankEntity = new DefaultRankEntity(new Rank("OLD", 1), null);
        when(defaultRepository.findAll()).thenReturn(List.of(defaultRankEntity));

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomLevels(false);
        webConfig.setCustomRanks("NEW");
        ranks.fillRanks(webConfig, List.of(rankEntity));

        assertThat(rankEntity.getRank(), equalTo(defaultRankEntity.getRank()));
    }

    @Test
    void testImportLevelsNull() {
        RankEntity rankEntity = new RankEntity(new Rank("OLD", 1), null, null);
        assertThrows(NullPointerException.class, () -> ranks.fillRanks(null, List.of(rankEntity)));
    }

    @Test
    void testImportLevelsNullField() {
        RankEntity rankEntity = new RankEntity(new Rank("OLD", 1), null, null);

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setCustomRanks(null);
        webConfig.setEnableCustomRanks(true);
        assertThrows(IllegalArgumentException.class, () -> ranks.fillRanks(webConfig, List.of(rankEntity)));
    }

    @Test
    void testImportLevelsEmpty() {
        RankEntity rankEntity = new RankEntity(new Rank("OLD", 1), null, null);

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableCustomRanks(true);
        webConfig.setCustomRanks("");
        assertThrows(IllegalArgumentException.class, () -> ranks.fillRanks(webConfig, List.of(rankEntity)));
    }
}
