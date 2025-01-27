package com.luxusxc.rank_up.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebConfig {
    private boolean enableAll;
    private boolean enableCustomLevels;
    private String customLevels;
    private String defaultLevels;
    private boolean enableCustomRanks;
    private String customRanks;
    private String defaultRanks;
    private boolean announceLevelUp;
    private String levelUpMessage;
    private String attachedImagesUrl;
}
