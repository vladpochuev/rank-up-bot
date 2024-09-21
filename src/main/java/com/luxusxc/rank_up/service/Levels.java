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

    public void importLevels(WebRankUpConfig webConfig) {
        if (webConfig.isEnableCustomLevels()) {
            setCustomLevels(webConfig.getCustomLevels());
        } else {
            setDefaultLevels();
        }
    }

    private void setCustomLevels(String customLevels) {
        List<Long> levels = getLevelsFromString(customLevels);
        setLevels(levels);
    }

    private List<Long> getLevelsFromString(String levels) {
        List<String> levelsStr = stringSplitter.split(levels, DELIMITER);
        if (levelsStr.equals(List.of())) throw new IllegalArgumentException();
        return levelsStr.stream().mapToLong(Long::parseLong).boxed().toList();
    }

    private void setDefaultLevels() {
        List<Long> levels = getDefaultLevels();
        setLevels(levels);
    }

    private List<Long> getDefaultLevels() {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        return defaultRanks.stream().map(DefaultRankEntity::getExperience).toList();
    }

    private void setLevels(List<Long> levels) {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        for (int i = 0; i < ranks.size(); i++) {
            RankEntity rank = ranks.get(i);
            rank.setExperience(levels.get(i));
        }
        repository.saveAll(ranks);
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
