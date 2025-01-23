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
public class Ranks {
    private static final String FILL_CUSTOM_LOG = "Ranks were replaced with custom ones";
    private static final String FILL_DEFAULT_LOG = "Ranks were replaced with default ones";
    private static final String CUSTOM_EXPORTED_LOG = "Custom ranks were successfully exported";
    private static final String DEFAULT_EXPORTED_LOG = "Default ranks were successfully exported";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CONFIG);

    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;
    private final DefaultRankEntityMapper defaultRankMapper;

    private static final String DELIMITER = "\n";

    public void fillRanks(WebConfig webConfig, List<RankEntity> ranks) {
        if (webConfig.isEnableCustomRanks()) {
            fillWithCustomRanks(ranks, webConfig.getCustomRanks());
        } else {
            fillFromDefaultRanks(ranks);
        }
    }

    private void fillWithCustomRanks(List<RankEntity> ranks, String rawRankNames) {
        List<String> rankNames = stringSplitter.split(rawRankNames, DELIMITER);
        fill(ranks, rankNames);
        log.info(LOG_MARKER, FILL_CUSTOM_LOG);
    }

    private void fillFromDefaultRanks(List<RankEntity> ranks) {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        List<String> defaultRankNames = getDefaultRankNames(defaultRanks);
        fill(ranks, defaultRankNames);
        log.info(LOG_MARKER, FILL_DEFAULT_LOG);
    }

    private void fill(List<RankEntity> ranks, List<String> rankNames) {
        for (int i = 0; i < ranks.size(); i++) {
            String rankName = rankNames.get(i).trim();
            ranks.get(i).setName(rankName);
        }
    }

    public String exportCustomRanks() {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        List<String> rankNames = getRankNames(ranks);
        log.info(LOG_MARKER, CUSTOM_EXPORTED_LOG);
        return stringJoiner.join(rankNames, DELIMITER);
    }

    public String exportDefaultRanks() {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        List<String> rankNames = getDefaultRankNames(defaultRanks);
        log.info(LOG_MARKER, DEFAULT_EXPORTED_LOG);
        return stringJoiner.join(rankNames, DELIMITER);
    }

    private List<String> getDefaultRankNames(List<DefaultRankEntity> defaultRanks) {
        List<RankEntity> ranks = defaultRanks.stream()
                .map(defaultRankMapper::toRankEntity)
                .toList();
        return getRankNames(ranks);
    }

    private List<String> getRankNames(List<RankEntity> ranks) {
        return ranks.stream()
                .map(rank -> {
                    String rankName = rank.getName();
                    throwIfRankNull(rankName);
                    return rankName;
                })
                .toList();
    }

    private void throwIfRankNull(String rankName) {
        if (rankName == null) {
            throw new NullPointerException();
        }
    }
}
