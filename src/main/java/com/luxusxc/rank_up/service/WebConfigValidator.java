package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WebConfigValidator {
    private static final String LEVELS_REGEX = "^\\s*(\\d+\\s*(,\\s*\\d+\\s*)*)?$";
    private static final String RANKS_REGEX = "^\\s*((\\S+(\\s+\\S+)*)\\s*(\\n\\s*(\\S+(\\s+\\S+)*)\\s*)*)?$";
    private static final String IMAGES_REGEX = "^\\s*(https?://\\S+\\s*(\\n\\s*https?://\\S+\\s*)*)?$";

    private static final String VALIDATION_START = "Start of the config validation";
    private static final String VALIDATION_STATUS_TEMPLATE = "Status of validation: %b";
    private static final String VALIDATION_ERROR_TEMPLATE = "Validation error: %s";

    private static final String LEVELS_VALIDATION_TEMPLATE = "Status of the levels validation: %b";
    private static final String RANKS_VALIDATION_TEMPLATE = "Status of the ranks validation: %b";
    private static final String IMAGES_VALIDATION_TEMPLATE = "Status of the images validation: %b";
    private static final String MESSAGE_VALIDATION_TEMPLATE = "Status of the levelUpMessage validation: %b";

    private static final String BOTH_CUSTOM_VALIDATION_TEMPLATE = "Both levels and ranks are custom. Is quantity valid: %b";
    private static final String CUSTOM_LEVELS_VALIDATION_TEMPLATE = "Only levels are custom. Is quantity valid: %b";
    private static final String CUSTOM_RANKS_VALIDATION_TEMPLATE = "Only ranks are custom. Is quantity valid: %b";
    private static final String BOTH_DEFAULT_VALIDATION = "Both levels and ranks are default. Quantity is valid";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.VALIDATION);

    private final DefaultRankRepository defaultRankRepository;
    private final StringSplitter splitter;

    public boolean isValid(WebRankUpConfig config) {
        try {
            log.info(LOG_MARKER, VALIDATION_START);
            boolean isValid = isLevelsValid(config) &&
                    isRanksValid(config) &&
                    isImagesValid(config) &&
                    isMessageValid(config) &&
                    isValidQuantity(config);
            log.info(LOG_MARKER, VALIDATION_STATUS_TEMPLATE.formatted(isValid));
            return isValid;
        } catch (Exception e) {
            log.error(LOG_MARKER, VALIDATION_ERROR_TEMPLATE.formatted(e.getMessage()));
            return false;
        }
    }

    private boolean isLevelsValid(WebRankUpConfig config) {
        if (!config.isEnableCustomLevels()) return true;
        String levels = config.getCustomLevels().trim();
        boolean isValid = !levels.isEmpty() && levels.matches(LEVELS_REGEX);
        log.info(LOG_MARKER, LEVELS_VALIDATION_TEMPLATE.formatted(isValid));
        return isValid;
    }

    private boolean isRanksValid(WebRankUpConfig config) {
        if (!config.isEnableCustomRanks()) return true;
        String ranks = config.getCustomRanks().trim();
        boolean isValid = !ranks.isEmpty() && ranks.matches(RANKS_REGEX);
        log.info(LOG_MARKER, RANKS_VALIDATION_TEMPLATE.formatted(isValid));
        return isValid;

    }

    private boolean isImagesValid(WebRankUpConfig config) {
        String images = config.getAttachedImagesUrl().trim();
        boolean isValid = images.isEmpty() || images.matches(IMAGES_REGEX);
        log.info(LOG_MARKER, IMAGES_VALIDATION_TEMPLATE.formatted(isValid));
        return isValid;
    }

    private boolean isMessageValid(WebRankUpConfig config) {
        boolean isValid = config.getLevelUpMessage() != null;
        log.info(LOG_MARKER, MESSAGE_VALIDATION_TEMPLATE.formatted(isValid));
        return isValid;
    }

    private boolean isValidQuantity(WebRankUpConfig config) {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRankRepository.findAll();
        boolean isValid;

        if (config.isEnableCustomRanks() && config.isEnableCustomLevels()) {
            int levelsSize = splitter.split(config.getCustomLevels(), ",").size();
            int ranksSize = splitter.split(config.getCustomRanks(), "\n").size();
            isValid = levelsSize == ranksSize;
            log.info(LOG_MARKER, BOTH_CUSTOM_VALIDATION_TEMPLATE.formatted(isValid));
        } else if (config.isEnableCustomLevels()) {
            int levelsSize = splitter.split(config.getCustomLevels(), ",").size();
            isValid = levelsSize <= defaultRanks.size();
            log.info(LOG_MARKER, CUSTOM_LEVELS_VALIDATION_TEMPLATE.formatted(isValid));
        } else if (config.isEnableCustomRanks()) {
            int ranksSize = splitter.split(config.getCustomRanks(), "\n").size();
            isValid = ranksSize <= defaultRanks.size();
            log.info(LOG_MARKER, CUSTOM_RANKS_VALIDATION_TEMPLATE.formatted(isValid));
        } else {
            isValid = true;
            log.info(LOG_MARKER, BOTH_DEFAULT_VALIDATION);
        }
        return isValid;
    }
}