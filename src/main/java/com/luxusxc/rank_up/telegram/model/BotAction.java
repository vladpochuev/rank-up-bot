package com.luxusxc.rank_up.telegram.model;

import com.luxusxc.rank_up.telegram.service.TelegramBot;

@FunctionalInterface
public interface BotAction extends Executable<TelegramBot> {
}
