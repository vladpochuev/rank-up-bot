package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.RankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LevelUpMessages {
    private final RankRepository repository;
    private final VariableReplacer variableReplacer;

    public void importMessage(WebRankUpConfig webConfig) {
        String message = webConfig.getLevelUpMessage();
        throwIfNullOrEmpty(message);
        saveCustomMessage(message);
    }

    private void throwIfNullOrEmpty(String str) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException();
        }
    }

    private void saveCustomMessage(String message) {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        for (RankEntity rank : ranks) {
            String messageWithRank = variableReplacer.replaceRankVars(message, rank);
            rank.setLevelUpMessage(messageWithRank);
        }
        repository.saveAll(ranks);
    }
}
