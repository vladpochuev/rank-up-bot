package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.*;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class Ranks {
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;
    private final RankEntityFactory rankFactory;
    private static final String DELIMITER = "\n";

    public void fillRanks(WebRankUpConfig webConfig, List<RankEntity> rankEntities) {
        if (webConfig.isEnableCustomRanks()) {
            fillWithCustomRanks(rankEntities, webConfig.getCustomRanks());
        } else {
            fillFromDefaultRanks(rankEntities);
        }
    }

    private void fillWithCustomRanks(List<RankEntity> rankEntities, String ranks) {
        List<String> rankNames = stringSplitter.split(ranks, DELIMITER);
        if (rankNames.equals(List.of())) throw new IllegalArgumentException();
        for (int i = 0; i < rankEntities.size(); i++) {
            rankEntities.get(i).setName(rankNames.get(i));
        }
    }

    private void fillFromDefaultRanks(List<RankEntity> rankEntities) {
        List<RankEntity> defaultRankEntities = getRankEntitiesFromDefault();
        for (int i = 0; i < rankEntities.size(); i++) {
            rankEntities.get(i).setName(defaultRankEntities.get(i).getName());
            rankEntities.get(i).setLevel(defaultRankEntities.get(i).getLevel());
        }
    }

    private List<RankEntity> getRankEntitiesFromDefault() {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        return rankFactory.mapDefaultRanksToRegular(defaultRanks);
    }

    public String exportRanks() {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        List<String> rankNames = getRankNames(ranks);
        return stringJoiner.join(rankNames, DELIMITER);
    }

    private List<String> getRankNames(List<RankEntity> ranks) {
        return ranks.stream()
                .map(rank -> {
                    String rankName = rank.getName();
                    if (rankName == null) {
                        throw new NullPointerException();
                    }
                    return rankName;
                })
                .toList();
    }
}
