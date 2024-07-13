package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.service.Executable;
import com.luxusxc.rank_up.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public abstract class Callback implements Executable<CallbackQuery> {
    protected TelegramBot bot;

    public Callback(TelegramBot bot) {
        this.bot = bot;
    }
}