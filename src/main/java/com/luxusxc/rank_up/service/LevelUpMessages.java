package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.repository.RankRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelUpMessages {
    private final RankRepository repository;
    private final RankUpConfig config;
    private final VariableReplacer variableReplacer;

    public LevelUpMessages(RankRepository repository, RankUpConfig config, VariableReplacer variableReplacer) {
        this.repository = repository;
        this.config = config;
        this.variableReplacer = variableReplacer;
    }

    public void importMessage(String message) {
        config.setLevelUpMessageFormat(message);
        saveCustomMessage(message);
    }

    private void saveCustomMessage(String message) {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        for (RankEntity rank : ranks) {
            String messageWithRank = variableReplacer.replaceRankVars(message, rank);
            rank.setLevelUpMessage(messageWithRank);
            repository.save(rank);
        }
    }
}
