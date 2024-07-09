package com.luxusxc.mirror_tg_ds.commands;

import com.luxusxc.mirror_tg_ds.service.Executable;
import com.luxusxc.mirror_tg_ds.service.TelegramBot;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public abstract class Command implements Executable<Message> {
    protected TelegramBot bot;
}