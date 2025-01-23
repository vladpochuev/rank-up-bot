package com.luxusxc.rank_up.telegram.callback;

import com.luxusxc.rank_up.telegram.model.Executable;
import com.luxusxc.rank_up.common.model.LogTags;
import com.luxusxc.rank_up.telegram.service.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@AllArgsConstructor
@Slf4j
public abstract class Callback implements Executable<CallbackQuery> {
    private static final String LOG_TEMPLATE = "%s was called (id=%d)";

    protected static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CALLBACK);
    protected TelegramBot bot;

    @Override
    public void execute(CallbackQuery callbackQuery) {
        String logMessage = LOG_TEMPLATE.formatted(this.getClass().getSimpleName(), callbackQuery.getFrom().getId());
        log.info(LOG_MARKER, logMessage);
        executeCallback(callbackQuery);
    }

    protected abstract void executeCallback(CallbackQuery callbackQuery);
}