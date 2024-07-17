package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.springframework.stereotype.Service;

@Service
public class Ranks {
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final RankUpConfig config;

    public Ranks(DefaultRankRepository defaultRepository, RankRepository repository, RankUpConfig config) {
        this.defaultRepository = defaultRepository;
        this.repository = repository;
        this.config = config;
    }

    public void importRanks(boolean enableCustom, String customRanks) {
        if (enableCustom) {
            config.setEnableCustomRanks(true);
            String[] ranks = customRanks.split(", ");
            repository.deleteAll();
            for (String rank : ranks) {
                RankEntity entity = new RankEntity(rank);
                repository.save(entity);
            }
        } else {
            config.setEnableCustomRanks(false);
            repository.deleteAll();
            Iterable<DefaultRankEntity> ranks = defaultRepository.findAll();
            for (DefaultRankEntity rank : ranks) {
                repository.save(new RankEntity(rank.getRank()));
            }
        }
    }

    public String exportRanks() {
        StringBuilder builder = new StringBuilder();
        Iterable<RankEntity> configEntities = config.getRankRepository().findAll();
        for (RankEntity configEntity : configEntities) {
            builder.append(configEntity.getRank()).append(", ");
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 2);
    }
}
