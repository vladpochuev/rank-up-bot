package com.luxusxc.rank_up.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebRankUpConfig {
    private boolean enableAll;
    private boolean enableCustomLevels;
    private String customLevels;
    private boolean enableCustomRanks;
    private String customRanks;
    private boolean announceLevelUp;
    private String levelUpMessage;
    private String attachedImagesUrl;

    public static WebRankUpConfig createFrom(RankUpConfig config) {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableAll(config.isEnableAll());
        webConfig.setEnableCustomRanks(config.isEnableCustomRanks());
        webConfig.setEnableCustomLevels(config.isEnableCustomLevels());
        webConfig.setAnnounceLevelUp(config.isAnnounceLevelUp());
        webConfig.setLevelUpMessage(config.getLevelUpMessageFormat());
        return webConfig;
    }
}
