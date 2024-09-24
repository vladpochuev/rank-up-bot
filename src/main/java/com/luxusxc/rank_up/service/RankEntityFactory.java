package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.RankEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankEntityFactory {
    public List<RankEntity> mapDefaultRanksToRegular(List<DefaultRankEntity> defaultRanks) {
        return defaultRanks
                .stream()
                .map(DefaultRankEntity::getRank)
                .map(RankEntity::new)
                .toList();
    }

}
