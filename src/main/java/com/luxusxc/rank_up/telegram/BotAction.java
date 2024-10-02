package com.luxusxc.rank_up.telegram;

import java.util.function.Function;

@FunctionalInterface
public interface BotAction {
    void run(TelegramBot bot);
}
