package com.luxusxc.rank_up.common.service.validation;

import com.luxusxc.rank_up.web.model.WebConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Order(3)
public class ImagesValidator implements Validator {
    private static final String IMAGES_REGEX = "^\\s*(https?://\\S+\\s*(\\n\\s*https?://\\S+\\s*)*)?$";
    private static final String SUCCESS_VALIDATION = "Validation of the images was success";
    private static final String FAILED_VALIDATION = "Validation of the images was failed";
    private static final String EXCEPTION_TEMPLATE = "Error while processing the levels: %s\n%s";

    @Override
    public boolean isValid(WebConfig config) {
        try {
            return checkImages(config);
        } catch (Exception e) {
            log.error(LOG_MARKER, EXCEPTION_TEMPLATE.formatted(e.getMessage(), e.getStackTrace()));
            return false;
        }
    }

    private boolean checkImages(WebConfig config) {
        String images = config.getAttachedImagesUrl().trim();
        boolean isValid = images.isEmpty() || images.matches(IMAGES_REGEX);
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
