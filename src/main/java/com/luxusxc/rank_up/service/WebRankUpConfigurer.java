package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.*;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WebRankUpConfigurer {
    private final RankRepository rankRepository;
    private final DefaultRankRepository defaultRankRepository;
    private final RankEntityFactory rankFactory;
    private final Ranks ranks;
    private final Levels levels;
    private final Images images;
    private final LevelUpMessages levelUpMessages;
    private final RankUpConfigHandler configHandler;

    public void importWebConfig(WebRankUpConfig webConfig) {
        serializeConfig(webConfig);
        saveRanks(webConfig);
    }

    private void serializeConfig(WebRankUpConfig webConfig) {
        configHandler.fillFromWebConfig(webConfig);
        configHandler.importConfig();
    }

    private void saveRanks(WebRankUpConfig webConfig) {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRankRepository.findAll();
        List<RankEntity> rankEntities = rankFactory.mapDefaultRanksToRegular(defaultRanks);
        importAllFrom(webConfig, rankEntities);
        save(rankEntities);
    }

    private void importAllFrom(WebRankUpConfig webConfig, List<RankEntity> rankEntities) {
        ranks.fillRanks(webConfig, rankEntities);
        levels.fillLevels(webConfig, rankEntities);
        levelUpMessages.fillMessage(webConfig, rankEntities);
        images.importImages(webConfig);
    }

    private void save(List<RankEntity> rankEntities) {
        rankRepository.truncate();
        rankRepository.saveAll(rankEntities);
    }

    public WebRankUpConfig exportWebConfig() {
        RankUpConfig config = configHandler.getConfig();
        WebRankUpConfig webConfig = WebRankUpConfig.createFrom(config);
        exportAllTo(webConfig);
        return webConfig;
    }

    private void exportAllTo(WebRankUpConfig webConfig) {
        webConfig.setCustomRanks(ranks.exportRanks());
        webConfig.setCustomLevels(levels.exportLevels());
        webConfig.setAttachedImagesUrl(images.exportImages());
    }
}
