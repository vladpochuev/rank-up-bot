package com.luxusxc.rank_up.common.service.validation;

import com.luxusxc.rank_up.common.model.LogTags;
import com.luxusxc.rank_up.web.model.WebConfig;
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
    private static final String VALIDATION_START = "Start of the config validation";
    private static final String VALIDATION_STATUS_TEMPLATE = "Status of validation: %b";
    private static final String VALIDATION_ERROR_TEMPLATE = "Validation error: %s\n%s";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.VALIDATION);

    private final List<Validator> validators;

    public boolean isValid(WebConfig config) {
        try {
            return checkValidity(config);
        } catch (Exception e) {
            log.error(LOG_MARKER, VALIDATION_ERROR_TEMPLATE.formatted(e.getMessage(), e.getStackTrace()));
            return false;
        }
    }

    private boolean checkValidity(WebConfig config) {
        log.info(LOG_MARKER, VALIDATION_START);
        boolean isValid = true;
        for (Validator validator : validators) {
            isValid &= validator.isValid(config);
        }
        log.info(LOG_MARKER, VALIDATION_STATUS_TEMPLATE.formatted(isValid));
        return isValid;
    }
}