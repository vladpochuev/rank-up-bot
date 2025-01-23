package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.web.mapper.DefaultRankEntityMapper;
import com.luxusxc.rank_up.web.model.DefaultRankEntity;
import com.luxusxc.rank_up.common.model.RankEntity;
import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.web.repository.DefaultRankRepository;
import com.luxusxc.rank_up.common.repository.RankRepository;
import com.luxusxc.rank_up.common.service.StringJoiner;
import com.luxusxc.rank_up.common.service.StringSplitter;
import com.luxusxc.rank_up.web.service.Ranks;
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
        this.ranks = new Ranks(repository, defaultRepository, new StringSplitter(), new StringJoiner(), DefaultRankEntityMapper.INSTANCE);
    }

    @Test
    void testExportRank() {
        List<RankEntity> rankEntities = new ArrayList<>();
        String levelUpMessage = "name: {name}";
        rankEntities.add(new RankEntity(1, "TEST1", 10L, levelUpMessage));

        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(ranks.exportCustomRanks(), equalTo("TEST1"));
    }

    @Test
    void testExportCustomRanks() {
        List<RankEntity> rankEntities = new ArrayList<>();
        String levelUpMessage = "name: {name}";
        rankEntities.add(new RankEntity(1, "TEST1", 10L, levelUpMessage));
        rankEntities.add(new RankEntity(2, "TEST2", 20L, levelUpMessage));
        rankEntities.add(new RankEntity(3, "TEST3", 30L, levelUpMessage));

        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(ranks.exportCustomRanks(), equalTo("TEST1\nTEST2\nTEST3"));
    }

    @Test
    void testExportCustomRanksEmpty() {
        List<RankEntity> rankEntities = new ArrayList<>();
        when(repository.findAll()).thenReturn(rankEntities);
        assertThat(ranks.exportCustomRanks(), equalTo(""));
    }

    @Test
    void testExportCustomRanksNull() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(null);
        when(repository.findAll()).thenReturn(rankEntities);
        assertThrows(NullPointerException.class, ranks::exportCustomRanks);
    }

    @Test
    void testExportCustomRanksNullRank() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(new RankEntity(null, 10L, "name: {name}"));
        when(repository.findAll()).thenReturn(rankEntities);
        assertThrows(NullPointerException.class, ranks::exportCustomRanks);
    }

    @Test
    void testExportCustomRanksNullRankName() {
        List<RankEntity> rankEntities = new ArrayList<>();
        rankEntities.add(new RankEntity(1, null, 10L, "name: {name}"));
        when(repository.findAll()).thenReturn(rankEntities);
        assertThrows(NullPointerException.class, ranks::exportCustomRanks);
    }

    @Test
    void testImportRanksCustom() {
        RankEntity rankEntity = new RankEntity(1, "OLD", null, null);

        WebConfig webConfig = new WebConfig();
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

        WebConfig webConfig = new WebConfig();
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

        WebConfig webConfig = new WebConfig();
        webConfig.setCustomRanks(null);
        webConfig.setEnableCustomRanks(true);
        assertThrows(IllegalArgumentException.class, () -> ranks.fillRanks(webConfig, List.of(rankEntity)));
    }
}
