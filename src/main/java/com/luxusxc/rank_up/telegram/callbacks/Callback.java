package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.model.Executable;
import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

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