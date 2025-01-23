package com.luxusxc.rank_up.common.service.validation;

import com.luxusxc.rank_up.web.model.WebConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Order(4)
public class LevelUpMessageValidator implements Validator {
    private static final String SUCCESS_VALIDATION = "Validation of the levelUpMessage was success";
    private static final String FAILED_VALIDATION = "Validation of the levelUpMessage was failed";

    @Override
    public boolean isValid(WebConfig config) {
        boolean isValid = config.getLevelUpMessage() != null;
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
