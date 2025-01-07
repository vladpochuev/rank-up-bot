package com.luxusxc.rank_up.service;

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
public class Ranks {
    private static final String FILL_CUSTOM_LOG = "Ranks were replaced with custom ones";
    private static final String FILL_DEFAULT_LOG = "Ranks were replaced with default ones";
    private static final String EXPORTED_LOG = "Ranks were successfully exported";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CONFIG);

    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;

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
        log.info(LOG_MARKER, FILL_CUSTOM_LOG);
    }

    private void fillFromDefaultRanks(List<RankEntity> rankEntities) {
        List<DefaultRankEntity> defaultRankEntities = (List<DefaultRankEntity>) defaultRepository.findAll();
        for (int i = 0; i < rankEntities.size(); i++) {
            rankEntities.get(i).setName(defaultRankEntities.get(i).getName());
            rankEntities.get(i).setLevel(defaultRankEntities.get(i).getLevel());
        }
        log.info(LOG_MARKER, FILL_DEFAULT_LOG);
    }

    public String exportRanks() {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        List<String> rankNames = getRankNames(ranks);
        log.info(LOG_MARKER, EXPORTED_LOG);
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
