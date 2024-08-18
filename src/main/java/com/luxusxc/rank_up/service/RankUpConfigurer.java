package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.WebRankUpConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Service
@AllArgsConstructor
public class RankUpConfigurer {
    private final Levels levels;
    private final Ranks ranks;
    private final Images images;
    private final LevelUpMessages levelUpMessages;
    private final RankUpConfig config;

    public void importConfig(WebRankUpConfig webConfig) {
        ranks.importRanks(webConfig.isEnableCustomRanks(), webConfig.getCustomRanks());
        levels.importLevels(webConfig.isEnableCustomLevels(), webConfig.getCustomLevels());
        levelUpMessages.importMessage(webConfig.getLevelUpMessage());
        images.importImages(webConfig.getAttachedImagesUrl());
        config.setEnableAll(webConfig.isEnableAll());
        config.setAnnounceLevelUp(webConfig.isAnnounceLevelUp());

        try(FileOutputStream fileOut = new FileOutputStream("src/main/resources/rankUpConfig.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WebRankUpConfig exportConfig() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableAll(config.isEnableAll());
        webConfig.setEnableCustomRanks(config.isEnableCustomRanks());
        webConfig.setCustomRanks(ranks.exportRanks());
        webConfig.setEnableCustomLevels(config.isEnableCustomLevels());
        webConfig.setCustomLevels(levels.exportLevels());
        webConfig.setAnnounceLevelUp(config.isAnnounceLevelUp());
        webConfig.setLevelUpMessage(config.getLevelUpMessageFormat());
        webConfig.setAttachedImagesUrl(images.exportImages());
        return webConfig;
    }
}
