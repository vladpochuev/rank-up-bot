package com.luxusxc.rank_up.common.service.validation;

import com.luxusxc.rank_up.web.model.DefaultRankEntity;
import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.web.repository.DefaultRankRepository;
import com.luxusxc.rank_up.common.service.StringSplitter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Order(5)
@AllArgsConstructor
public class QuantityValidator implements Validator {
    private static final String BOTH_CUSTOM_VALIDATION = "Both levels and ranks are custom";
    private static final String CUSTOM_LEVELS_VALIDATION = "Only levels are custom";
    private static final String CUSTOM_RANKS_VALIDATION = "Only ranks are custom";
    private static final String BOTH_DEFAULT_VALIDATION = "Both levels and ranks are default";
    private static final String SUCCESS_VALIDATION = "Validation of the levels and ranks quantity was success";
    private static final String FAILED_VALIDATION = "Validation of the levels and ranks quantity was failed";

    private final DefaultRankRepository defaultRankRepository;
    private final StringSplitter splitter;

    @Override
    public boolean isValid(WebConfig config) {
        if (config.isEnableCustomLevels() || config.isEnableCustomRanks()) {
            List<String> levels = getSplitValues(config.getCustomLevels(), ",");
            List<String> ranks = getSplitValues(config.getCustomRanks(), "\n");
            boolean isValid = checkQuantity(levels, ranks);
            logStatus(isValid);
            return isValid;
        } else {
            log.info(LOG_MARKER, BOTH_DEFAULT_VALIDATION);
            return true;
        }
    }

    private List<String> getSplitValues(String input, String delimiter) {
        if (input != null) {
            return splitter.split(input, delimiter);
        }
        return null;
    }

    private boolean checkQuantity(List<String> levels, List<String> ranks) {
        if (levels != null && ranks != null) {
            boolean isValid = levels.size() == ranks.size();
            log.info(LOG_MARKER, BOTH_CUSTOM_VALIDATION);
            return isValid;
        }
        return checkSingleCustomQuantity(levels, ranks);
    }

    private boolean checkSingleCustomQuantity(List<String> levels, List<String> ranks) {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRankRepository.findAll();
        int customSize = levels != null ? levels.size() : ranks.size();
        boolean isValid = customSize <= defaultRanks.size();
        if (levels != null) {
            log.info(LOG_MARKER, CUSTOM_LEVELS_VALIDATION);
        } else {
            log.info(LOG_MARKER, CUSTOM_RANKS_VALIDATION);
        }
        return isValid;
    }

    private void logStatus(boolean status) {
        if (status) {
            log.info(LOG_MARKER, SUCCESS_VALIDATION);
        } else {
            log.error(LOG_MARKER, FAILED_VALIDATION);
        }
    }
}


