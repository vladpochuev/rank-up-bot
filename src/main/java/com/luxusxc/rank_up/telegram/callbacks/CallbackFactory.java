package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CallbackFactory {
    private final TelegramBot bot;

    public Callback getCallback(CallbackType callback) {
        if (callback == null) throw new IllegalArgumentException();

        switch (callback) {
            default -> throw new IllegalArgumentException();
        }
    }
}
