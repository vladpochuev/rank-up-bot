package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.model.Executable;
import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
@Slf4j
public abstract class Command implements Executable<Message> {
    private static final String LOG_TEMPLATE = "%s was called (id=%d)";

    protected static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.COMMAND);
    protected TelegramBot bot;

    @Override
    public void execute(Message message) {
        String logMessage = LOG_TEMPLATE.formatted(this.getClass().getSimpleName(), message.getChatId());
        log.info(LOG_MARKER, logMessage);
        executeCommand(message);
    }

    protected abstract void executeCommand(Message message);
}