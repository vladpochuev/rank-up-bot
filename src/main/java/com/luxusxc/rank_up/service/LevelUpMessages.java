package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelUpMessages {
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final RankUpConfig config;

    public LevelUpMessages(DefaultRankRepository defaultRepository, RankRepository repository, RankUpConfig config) {
        this.defaultRepository = defaultRepository;
        this.repository = repository;
        this.config = config;
    }

    public void importMessage(String message) {
        config.setLevelUpMessageFormat(message);
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        for (int i = 0; i < ranks.size(); i++) {
            RankEntity rank = ranks.get(i);
            String messageWithLevel = message.replaceAll("\\{newlvl}", String.valueOf(i + 1));
            String messageWithRank = messageWithLevel.replaceAll("\\{newrank}", rank.getRank());
            rank.setLevelUpMessage(messageWithRank);
            repository.save(rank);
        }
    }
}
