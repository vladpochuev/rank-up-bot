package com.luxusxc.rank_up.web.service;

import com.luxusxc.rank_up.web.mapper.DefaultRankEntityMapper;
import com.luxusxc.rank_up.web.model.DefaultRankEntity;
import com.luxusxc.rank_up.common.model.LogTags;
import com.luxusxc.rank_up.common.model.RankEntity;
import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.web.repository.DefaultRankRepository;
import com.luxusxc.rank_up.common.repository.RankRepository;
import com.luxusxc.rank_up.common.service.StringJoiner;
import com.luxusxc.rank_up.common.service.StringSplitter;
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

    public void fillLevels(WebConfig webConfig, List<RankEntity> ranks) {
        if (webConfig.isEnableCustomLevels()) {
            fillWithCustomExp(ranks, webConfig.getCustomLevels());
        } else {
            fillFromDefaultExp(ranks);
        }
    }

    private void fillWithCustomExp(List<RankEntity> ranks, String rawExp) {
        List<Long> expValues = getExpFromString(rawExp);
        fill(ranks, expValues);
        log.info(LOG_MARKER, FILL_CUSTOM_LOG);
    }

    private List<Long> getExpFromString(String rawExp) {
        List<String> expStringList = stringSplitter.split(rawExp, IMPORT_DELIMITER);
        throwIfListEmpty(expStringList);
        return expStringList.stream()
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .boxed()
                .toList();
    }

    private void throwIfListEmpty(List<String> list) {
        if (list.equals(List.of())) {
            throw new IllegalArgumentException();
        }
    }

    private void fillFromDefaultExp(List<RankEntity> ranks) {
        List<Long> defaultExpValues = getDefaultExp();
        fill(ranks, defaultExpValues);
        log.info(LOG_MARKER, FILL_DEFAULT_LOG);
    }

    private List<Long> getDefaultExp() {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        return defaultRanks.stream()
                .map(DefaultRankEntity::getExperience)
                .toList();
    }

    private void fill(List<RankEntity> ranks, List<Long> exp) {
        for (int i = 0; i < ranks.size(); i++) {
            RankEntity rank = ranks.get(i);
            rank.setLevel(i + 1);
            rank.setExperience(exp.get(i));
        }
    }

    public String exportCustomLevels() {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        List<String> expStringValues = getExpFromRanks(ranks);
        log.info(LOG_MARKER, CUSTOM_EXPORTED_LOG);
        return stringJoiner.join(expStringValues, EXPORT_DELIMITER);
    }

    public String exportDefaultLevels() {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        List<RankEntity> ranks = defaultRanks.stream()
                .map(defaultRankMapper::toRankEntity)
                .toList();
        List<String> expStrings = getExpFromRanks(ranks);
        log.info(LOG_MARKER, DEFAULT_EXPORTED_LOG);
        return stringJoiner.join(expStrings, EXPORT_DELIMITER);
    }

    private List<String> getExpFromRanks(List<RankEntity> ranks) {
        return ranks.stream()
                .map(rankEntity -> {
                    Long exp = rankEntity.getExperience();
                    throwIfExpNull(exp);
                    return exp;
                })
                .map(String::valueOf)
                .toList();
    }

    private void throwIfExpNull(Long exp) {
        if (exp == null) {
            throw new NullPointerException();
        }
    }
}
