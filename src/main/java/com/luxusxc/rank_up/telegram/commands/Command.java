package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.model.Executable;
import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public abstract class Command implements Executable<Message> {
    protected TelegramBot bot;
}