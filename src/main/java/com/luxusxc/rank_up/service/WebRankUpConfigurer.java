package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.RankUpConfig;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WebRankUpConfigurer {
    private final Ranks ranks;
    private final Levels levels;
    private final Images images;
    private final LevelUpMessages levelUpMessages;
    private final RankUpConfigHandler configHandler;

    public void importWebConfig(WebRankUpConfig webConfig) {
        configHandler.fillFromWebConfig(webConfig);

        ranks.importRanks(webConfig);
        levels.importLevels(webConfig);
        levelUpMessages.importMessage(webConfig);
        images.importImages(webConfig);

        configHandler.importConfig();
    }

    public WebRankUpConfig exportWebConfig() {
        RankUpConfig config = configHandler.getConfig();
        WebRankUpConfig webConfig = WebRankUpConfig.createFrom(config);

        webConfig.setCustomRanks(ranks.exportRanks());
        webConfig.setCustomLevels(levels.exportLevels());
        webConfig.setAttachedImagesUrl(images.exportImages());

        return webConfig;
    }
}
