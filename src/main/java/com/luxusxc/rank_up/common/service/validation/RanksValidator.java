package com.luxusxc.rank_up.common.service.validation;

import com.luxusxc.rank_up.web.model.WebConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Order(2)
public class RanksValidator implements Validator {
    private static final String RANKS_REGEX = "^\\s*((\\S+(\\s+\\S+)*)\\s*(\\n\\s*(\\S+(\\s+\\S+)*)\\s*)*)?$";
    private static final String SUCCESS_VALIDATION = "Validation of the ranks was success";
    private static final String FAILED_VALIDATION = "Validation of the ranks was failed";
    private static final String EXCEPTION_TEMPLATE = "Error while processing the ranks: %s\n%s";

    @Override
    public boolean isValid(WebConfig config) {
        try {
            return checkRanks(config);
        } catch (Exception e) {
            log.error(LOG_MARKER, EXCEPTION_TEMPLATE.formatted(e.getMessage(), e.getStackTrace()));
            return false;
        }
    }

    private boolean checkRanks(WebConfig config) {
        if (!config.isEnableCustomRanks()) return true;
        String ranks = config.getCustomRanks().trim();
        boolean isValid = !ranks.isEmpty() && ranks.matches(RANKS_REGEX);
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
