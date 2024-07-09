package com.luxusxc.mirror_tg_ds.callbacks;

import com.luxusxc.mirror_tg_ds.service.TelegramBot;
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
