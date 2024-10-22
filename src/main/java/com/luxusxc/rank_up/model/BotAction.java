package com.luxusxc.rank_up.model;

import com.luxusxc.rank_up.telegram.TelegramBot;

@FunctionalInterface
public interface BotAction extends Executable<TelegramBot> {
}
