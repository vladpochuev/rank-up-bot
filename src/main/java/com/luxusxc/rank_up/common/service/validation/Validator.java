package com.luxusxc.rank_up.common.service.validation;

import com.luxusxc.rank_up.common.model.LogTags;
import com.luxusxc.rank_up.web.model.WebConfig;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public interface Validator {
    Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.VALIDATION);

    boolean isValid(WebConfig config);
}
