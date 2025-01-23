package com.luxusxc.rank_up.telegram.callback;

import com.luxusxc.rank_up.telegram.service.TelegramBot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CallbackFactory {
    private final TelegramBot bot;

    public Callback getCallback(CallbackType callback) {
        if (callback == null) throw new IllegalArgumentException();

        switch (callback) {
            case GET_CHAT -> {
                return new GetChatCallback(bot);
            }
            case BACK -> {
                return new BackCallback(bot);
            }
        }
        throw new IllegalArgumentException();
    }
}
