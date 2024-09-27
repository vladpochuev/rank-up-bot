package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.service.Executable;
import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@AllArgsConstructor
public abstract class Callback implements Executable<CallbackQuery> {
    protected TelegramBot bot;
}