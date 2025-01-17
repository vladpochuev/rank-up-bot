package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.BotAction;
import com.luxusxc.rank_up.service.CallbackParser;
import com.luxusxc.rank_up.telegram.callbacks.Callback;
import com.luxusxc.rank_up.telegram.callbacks.CallbackFactory;
import com.luxusxc.rank_up.telegram.callbacks.CallbackType;
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
