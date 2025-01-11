package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LevelUpMessages {
    private static final String FILL_LOG = "Level up messages were replaced with new ones";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CONFIG);

    private final VariableReplacer variableReplacer;

    public void fillMessage(WebRankUpConfig webConfig, List<RankEntity> rankEntities) {
        String message = webConfig.getLevelUpMessage();
        throwIfNullOrEmpty(message);
        saveCustomMessage(rankEntities, message);
        log.info(LOG_MARKER, FILL_LOG);
    }

    private void throwIfNullOrEmpty(String str) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException();
        }
    }

    private void saveCustomMessage(List<RankEntity> rankEntities, String message) {
        for (RankEntity rank : rankEntities) {
            String messageWithRank = variableReplacer.replaceRankVars(message, rank);
            rank.setLevelUpMessage(messageWithRank);
        }
    }
}
