package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.glassfish.hk2.api.Rank;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Ranks {
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final RankUpConfig config;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;
    private static final String SEPARATOR = ", ";

    public Ranks(DefaultRankRepository defaultRepository, RankRepository repository, RankUpConfig config, StringSplitter stringSplitter, StringJoiner stringJoiner) {
        this.defaultRepository = defaultRepository;
        this.repository = repository;
        this.config = config;
        this.stringSplitter = stringSplitter;
        this.stringJoiner = stringJoiner;
    }

    public void importRanks(boolean enableCustom, String customRanks) {
        if (enableCustom) {
            setCustomRanks(customRanks);
        } else {
            setDefaultRanks();
        }
    }

    private void setCustomRanks(String customRanks) {
        config.setEnableCustomRanks(true);
        List<RankEntity> ranks = getRanksFromString(customRanks);
        setRanks(ranks);
    }

    private List<RankEntity> getRanksFromString(String ranks) {
        List<String> rankNames = stringSplitter.split(ranks, SEPARATOR);
        return rankNames.stream().map(RankEntity::new).toList();
    }

    private void setDefaultRanks() {
        config.setEnableCustomRanks(false);
        List<DefaultRankEntity> defaultRanks = getDefaultRanks();
        List<RankEntity> ranks = mapDefaultRanksToRegular(defaultRanks);
        setRanks(ranks);
    }

    private List<DefaultRankEntity> getDefaultRanks() {
        return (List<DefaultRankEntity>) defaultRepository.findAll();
    }

    private List<RankEntity> mapDefaultRanksToRegular(List<DefaultRankEntity> defaultRanks) {
        return defaultRanks.stream().map(DefaultRankEntity::getRank).map(RankEntity::new).toList();
    }

    private void setRanks(List<RankEntity> ranks) {
        repository.deleteAll();
        repository.saveAll(ranks);
    }

    public String exportRanks() {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        List<String> rankNames = getRankNames(ranks);
        return stringJoiner.join(rankNames, SEPARATOR);
    }

    private List<String> getRankNames(List<RankEntity> ranks) {
        return ranks.stream().map(RankEntity::getRank).toList();
    }
}
