package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.mapper.DefaultRankEntityMapper;
import com.luxusxc.rank_up.mapper.RankUpConfigMapper;
import com.luxusxc.rank_up.model.*;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class WebRankUpConfigurer {
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CONFIG);
    private static final String CREATED_LOG = "Created new config from web form";
    private static final String START_SAVE_LOG = "Started saving the settings to the database";
    private static final String SUCCESS_SAVE_LOG = "New settings was saved to the database";
    private static final String CONVERTED_TO_WEB_CONFIG_LOG = "Config was successfully received and converted to the web config";
    private static final String DATABASE_DATA_MAPPED_LOG = "Database data was successfully received and mapped to the web config";

    private final RankRepository rankRepository;
    private final DefaultRankRepository defaultRankRepository;
    private final DefaultRankEntityMapper defaultRankEntityMapper;
    private final Ranks ranks;
    private final Levels levels;
    private final Images images;
    private final LevelUpMessages levelUpMessages;
    private final RankUpConfigHandler configHandler;
    private final RankUpConfigMapper configMapper;

    public void importWebConfig(WebRankUpConfig webConfig) {
        serializeConfig(webConfig);
        saveRanks(webConfig);
    }

    private void serializeConfig(WebRankUpConfig webConfig) {
        configHandler.fillFromWebConfig(webConfig);
        log.info(LOG_MARKER, CREATED_LOG);
        configHandler.importConfig();
    }

    private void saveRanks(WebRankUpConfig webConfig) {
        log.info(LOG_MARKER, START_SAVE_LOG);
        List<DefaultRankEntity> defaultRankEntities = (List<DefaultRankEntity>) defaultRankRepository.findAll();
        List<RankEntity> rankEntities = getRankEntitiesFromDefault(defaultRankEntities);
        importAllFrom(webConfig, rankEntities);
        save(rankEntities);
        log.info(LOG_MARKER, SUCCESS_SAVE_LOG);
    }

    private List<RankEntity> getRankEntitiesFromDefault(List<DefaultRankEntity> defaultRankEntities) {
        return defaultRankEntities.stream()
                .map(defaultRankEntityMapper::toRankEntity)
                .toList();
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
        WebRankUpConfig webConfig = configMapper.toWebRankUpConfig(config);
        log.info(LOG_MARKER, CONVERTED_TO_WEB_CONFIG_LOG);
        exportAllTo(webConfig);
        log.info(LOG_MARKER, DATABASE_DATA_MAPPED_LOG);
        return webConfig;
    }

    private void exportAllTo(WebRankUpConfig webConfig) {
        webConfig.setCustomRanks(ranks.exportRanks());
        webConfig.setCustomLevels(levels.exportLevels());
        webConfig.setAttachedImagesUrl(images.exportImages());
    }
}
