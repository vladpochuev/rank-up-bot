package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.mapper.DefaultRankEntityMapper;
import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class Levels {
    private static final String FILL_CUSTOM_LOG = "Levels were replaced with custom ones";
    private static final String FILL_DEFAULT_LOG = "Levels were replaced with default ones";
    private static final String CUSTOM_EXPORTED_LOG = "Custom levels were successfully exported";
    private static final String DEFAULT_EXPORTED_LOG = "Default levels were successfully exported";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CONFIG);

    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;
    private final DefaultRankEntityMapper defaultRankMapper;

    private static final String IMPORT_DELIMITER = ",";
    private static final String EXPORT_DELIMITER = ", ";

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
        log.info(LOG_MARKER, FILL_CUSTOM_LOG);
    }

    private List<Long> getLevelsFromString(String levels) {
        List<String> levelsStr = stringSplitter.split(levels, IMPORT_DELIMITER);
        if (levelsStr.equals(List.of())) throw new IllegalArgumentException();
        return levelsStr.stream().map(String::trim).mapToLong(Long::parseLong).boxed().toList();
    }

    private void fillFromDefaultLevels(List<RankEntity> rankEntities) {
        List<Long> defaultLevels = getDefaultLevels();
        for (int i = 0; i < rankEntities.size(); i++) {
            RankEntity rank = rankEntities.get(i);
            rank.setLevel(i+1);
            rank.setExperience(defaultLevels.get(i));
        }
        log.info(LOG_MARKER, FILL_DEFAULT_LOG);
    }

    private List<Long> getDefaultLevels() {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        return defaultRanks.stream().map(DefaultRankEntity::getExperience).toList();
    }

    public String exportCustomLevels() {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        List<String> expStrings = getExpFromRanks(ranks);
        log.info(LOG_MARKER, CUSTOM_EXPORTED_LOG);
        return stringJoiner.join(expStrings, EXPORT_DELIMITER);
    }

    public String exportDefaultLevels() {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        List<RankEntity> ranks = defaultRanks.stream().map(defaultRankMapper::toRankEntity).toList();
        List<String> expStrings = getExpFromRanks(ranks);
        log.info(LOG_MARKER, DEFAULT_EXPORTED_LOG);
        return stringJoiner.join(expStrings, EXPORT_DELIMITER);
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
