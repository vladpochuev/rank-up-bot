package com.luxusxc.mirror_tg_ds.callbacks;

import com.luxusxc.mirror_tg_ds.service.Executable;
import com.luxusxc.mirror_tg_ds.service.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public abstract class Callback implements Executable<CallbackQuery> {
    protected TelegramBot bot;

    public Callback(TelegramBot bot) {
        this.bot = bot;
    }
}