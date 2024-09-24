package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LevelUpMessages {
    private final VariableReplacer variableReplacer;

    public void fillMessage(WebRankUpConfig webConfig, List<RankEntity> rankEntities) {
        String message = webConfig.getLevelUpMessage();
        throwIfNullOrEmpty(message);
        saveCustomMessage(rankEntities, message);
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
