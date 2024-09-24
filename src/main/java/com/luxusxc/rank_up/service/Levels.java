package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class Levels {
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;
    private static final String DELIMITER = ", ";

    public void fillLevels(WebRankUpConfig webConfig, List<RankEntity> rankEntities) {
        if (webConfig.isEnableCustomLevels()) {
            fillWithCustomLevels(rankEntities, webConfig.getCustomLevels());
        } else {
            fillFromDefaultLevels(rankEntities);
        }
    }

    private void fillWithCustomLevels(List<RankEntity> rankEntities, String levelsStr) {
        List<Long> levels = getLevelsFromString(levelsStr);
        for (int i = 0; i < rankEntities.size(); i++) {
            rankEntities.get(i).setExperience(levels.get(i));
        }
    }

    private List<Long> getLevelsFromString(String levels) {
        List<String> levelsStr = stringSplitter.split(levels, DELIMITER);
        if (levelsStr.equals(List.of())) throw new IllegalArgumentException();
        return levelsStr.stream().mapToLong(Long::parseLong).boxed().toList();
    }

    private void fillFromDefaultLevels(List<RankEntity> rankEntities) {
        List<Long> defaultLevels = getDefaultLevels();
        for (int i = 0; i < rankEntities.size(); i++) {
            rankEntities.get(i).setExperience(defaultLevels.get(i));
        }
    }

    private List<Long> getDefaultLevels() {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        return defaultRanks.stream().map(DefaultRankEntity::getExperience).toList();
    }

    public String exportLevels() {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        List<String> expStrings = getExpFromRanks(ranks);
        return stringJoiner.join(expStrings, DELIMITER);
    }

    private List<String> getExpFromRanks(List<RankEntity> ranks) {
        return ranks.stream()
                .map(rankEntity -> {
                    Long experience = rankEntity.getExperience();
                    if (experience == null) {
                        throw new NullPointerException();
                    }
                    return experience;
                })
                .map(String::valueOf)
                .toList();
    }
}
