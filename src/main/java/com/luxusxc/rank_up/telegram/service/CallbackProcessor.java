package com.luxusxc.rank_up.telegram.service;

import com.luxusxc.rank_up.telegram.model.BotAction;
import com.luxusxc.rank_up.telegram.callback.Callback;
import com.luxusxc.rank_up.telegram.callback.CallbackFactory;
import com.luxusxc.rank_up.telegram.callback.CallbackType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
@AllArgsConstructor
public class CallbackProcessor {
    private final CallbackParser callbackParser;

    public BotAction processCallback(CallbackQuery callbackQuery) {
        return bot -> {
            String value = callbackQuery.getData();
            String prefix = callbackParser.getPrefix(value);
            CallbackFactory callbackFactory = new CallbackFactory(bot);
            CallbackType callbackType = CallbackType.getInstance(prefix);
            Callback callback = callbackFactory.getCallback(callbackType);
            callback.execute(callbackQuery);
        };
    }
}
