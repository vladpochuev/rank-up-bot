package com.luxusxc.rank_up.common.service.validation;

import com.luxusxc.rank_up.web.model.WebConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Order(1)
public class LevelsValidator implements Validator {
    private static final String LEVELS_REGEX = "^\\s*(\\d+\\s*(,\\s*\\d+\\s*)*)?$";
    private static final String SUCCESS_VALIDATION = "Validation of the levels was success";
    private static final String FAILED_VALIDATION = "Validation of the levels was failed";
    private static final String EXCEPTION_TEMPLATE = "Error while processing the levels: %s\n%s";

    @Override
    public boolean isValid(WebConfig config) {
        try {
            return checkLevels(config);
        } catch (Exception e) {
            log.error(LOG_MARKER, EXCEPTION_TEMPLATE.formatted(e.getMessage(), e.getStackTrace()));
            return false;
        }
    }

    private boolean checkLevels(WebConfig config) {
        if (!config.isEnableCustomLevels()) return true;
        String levels = config.getCustomLevels().trim();
        boolean isValid = !levels.isEmpty() && levels.matches(LEVELS_REGEX);
        logStatus(isValid);
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
