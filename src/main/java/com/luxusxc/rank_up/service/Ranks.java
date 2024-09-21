package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.*;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class Ranks {
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;
    private static final String DELIMITER = "\n";

    public void importRanks(WebRankUpConfig webConfig) {
        if (webConfig.isEnableCustomRanks()) {
            setCustomRanks(webConfig.getCustomRanks());
        } else {
            setDefaultRanks();
        }
    }

    private void setCustomRanks(String customRanks) {
        List<RankEntity> ranks = getRanksFromString(customRanks);
        setRanks(ranks);
    }

    private List<RankEntity> getRanksFromString(String ranks) {
        AtomicInteger i = new AtomicInteger(1);
        List<String> rankNames = stringSplitter.split(ranks, DELIMITER);
        if (rankNames.equals(List.of())) throw new IllegalArgumentException();
        return rankNames.stream().map(a -> new Rank(a, i.getAndIncrement())).map(RankEntity::new).toList();
    }

    private void setDefaultRanks() {
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
        for (RankEntity rankEntity : ranks) {
            if (rankEntity.getRank().getRankName().equals("")) {
                throw new IllegalArgumentException();
            }
        }
        repository.deleteAll();
        repository.saveAll(ranks);
    }

    public String exportRanks() {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        List<String> rankNames = getRankNames(ranks);
        return stringJoiner.join(rankNames, DELIMITER);
    }

    private List<String> getRankNames(List<RankEntity> ranks) {
        return ranks.stream()
                .map(RankEntity::getRank)
                .map(rank -> {
                    String rankName = rank.getRankName();
                    if (rankName == null) {
                        throw new NullPointerException();
                    }
                    return rankName;
                })
                .toList();
    }
}
