package com.luxusxc.rank_up.web.service;

import com.luxusxc.rank_up.web.mapper.ConfigMapper;
import com.luxusxc.rank_up.common.model.Config;
import com.luxusxc.rank_up.common.model.LogTags;
import com.luxusxc.rank_up.common.model.RankEntity;
import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.web.repository.DefaultRankRepository;
import com.luxusxc.rank_up.common.repository.RankRepository;
import com.luxusxc.rank_up.common.service.ConfigHandler;
import com.luxusxc.rank_up.common.service.StringSplitter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class WebConfigHandler {
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CONFIG);
    private static final String CREATED_LOG = "Created new config from web form";
    private static final String START_SAVE_LOG = "Started saving the settings to the database";
    private static final String SUCCESS_SAVE_LOG = "New settings was saved to the database";
    private static final String CONVERTED_TO_WEB_CONFIG_LOG = "Config was successfully received and converted to the web config";
    private static final String DATABASE_DATA_MAPPED_LOG = "Database data was successfully received and mapped to the web config";

    private final RankRepository rankRepository;
    private final DefaultRankRepository defaultRankRepository;
    private final Ranks ranks;
    private final Levels levels;
    private final Images images;
    private final LevelUpMessages levelUpMessages;
    private final ConfigHandler configHandler;
    private final ConfigMapper configMapper;
    private final StringSplitter splitter;

    public void importWebConfig(WebConfig webConfig) {
        serializeConfig(webConfig);
        saveRanks(webConfig);
    }

    private void serializeConfig(WebConfig webConfig) {
        configHandler.fillFromWebConfig(webConfig);
        log.info(LOG_MARKER, CREATED_LOG);
        configHandler.importConfig();
    }

    private void saveRanks(WebConfig webConfig) {
        log.info(LOG_MARKER, START_SAVE_LOG);
        List<RankEntity> rankEntities = getRankEntities(webConfig);
        importAllFrom(webConfig, rankEntities);
        save(rankEntities);
        log.info(LOG_MARKER, SUCCESS_SAVE_LOG);
    }

    private List<RankEntity> getRankEntities(WebConfig webConfig) {
        if (webConfig.isEnableCustomRanks() || webConfig.isEnableCustomLevels()) {
            List<String> levels = getSplitValues(webConfig.getCustomLevels(), ",");
            List<String> ranks = getSplitValues(webConfig.getCustomRanks(), "\n");
            return validateAndCreateEntities(levels, ranks);
        } else {
            int defaultSize = (int) defaultRankRepository.count();
            return createNRankEntities(defaultSize);
        }
    }

    private List<String> getSplitValues(String input, String delimiter) {
        if (input != null) {
            return splitter.split(input, delimiter);
        }
        return null;
    }

    private List<RankEntity> validateAndCreateEntities(List<String> levels, List<String> ranks) {
        if (levels != null && ranks != null) {
            checkQuantity(levels, ranks);
        }
        return createNRankEntities(levels != null ? levels.size() : ranks.size());
    }

    private void checkQuantity(List<String> levels, List<String> ranks) {
        int levelsQuantity = levels.size();
        int ranksQuantity = ranks.size();

        if (levelsQuantity != ranksQuantity) {
            throw new IllegalArgumentException();
        }
    }

    private List<RankEntity> createNRankEntities(int n) {
        List<RankEntity> levelsEntities = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            levelsEntities.add(new RankEntity());
        }
        return levelsEntities;
    }

    private void importAllFrom(WebConfig webConfig, List<RankEntity> rankEntities) {
        ranks.fillRanks(webConfig, rankEntities);
        levels.fillLevels(webConfig, rankEntities);
        levelUpMessages.fillMessage(webConfig, rankEntities);
        images.importImages(webConfig);
    }

    private void save(List<RankEntity> rankEntities) {
        rankRepository.truncate();
        rankRepository.saveAll(rankEntities);
    }

    public WebConfig exportWebConfig() {
        WebConfig webConfig = getSettingsWebConfig();
        exportAllTo(webConfig);
        return webConfig;
    }

    private WebConfig getSettingsWebConfig() {
        Config config = configHandler.getConfig();
        WebConfig webConfig = configMapper.toWebRankUpConfig(config);
        log.info(LOG_MARKER, CONVERTED_TO_WEB_CONFIG_LOG);
        return webConfig;
    }

    private void exportAllTo(WebConfig webConfig) {
        webConfig.setCustomRanks(ranks.exportCustomRanks());
        webConfig.setDefaultRanks(ranks.exportDefaultRanks());
        webConfig.setCustomLevels(levels.exportCustomLevels());
        webConfig.setDefaultLevels(levels.exportDefaultLevels());
        webConfig.setAttachedImagesUrl(images.exportImages());
        log.info(LOG_MARKER, DATABASE_DATA_MAPPED_LOG);
    }
}
