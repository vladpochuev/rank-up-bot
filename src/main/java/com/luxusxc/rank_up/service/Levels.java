package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class Levels {
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final RankUpConfig config;

    public Levels(DefaultRankRepository defaultRepository, RankRepository repository, RankUpConfig config) {
        this.defaultRepository = defaultRepository;
        this.repository = repository;
        this.config = config;
    }

    public void importLevels(boolean enableCustom, String customLevels) {
        if (enableCustom) {
            config.setEnableCustomLevels(true);
            String[] levelsSplit = customLevels.split(", ");
            long[] levels = Arrays.stream(levelsSplit).mapToLong(Long::parseLong).toArray();
            List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
            for (int i = 0; i < ranks.size(); i++) {
                RankEntity rank = ranks.get(i);
                rank.setExperience(levels[i]);
                repository.save(rank);
            }
        } else {
            config.setEnableCustomLevels(false);
            List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
            List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
            for (int i = 0; i < ranks.size(); i++) {
                RankEntity rank = ranks.get(i);
                DefaultRankEntity defaultRank = defaultRanks.get(i);
                rank.setExperience(defaultRank.getExperience());
                repository.save(rank);
            }
        }
    }

    public String exportLevels() {
        StringBuilder builder = new StringBuilder();
        Iterable<RankEntity> ranks = config.getRankRepository().findAll();
        for (RankEntity rank : ranks) {
            builder.append(rank.getExperience()).append(", ");
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 2);
    }
}
