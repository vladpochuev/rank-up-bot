package com.luxusxc.rank_up.telegram.callbacks;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum CallbackType {
    GET_CHAT("CHAT");

    public final String prefix;

    private static final Map<String, CallbackType> PREFIX_TO_CALLBACK;

    static {
        PREFIX_TO_CALLBACK = new HashMap<>();
        for (CallbackType callback : values()) {
            PREFIX_TO_CALLBACK.put(callback.prefix, callback);
        }
    }

    public static CallbackType getInstance(String prefix) {
        return PREFIX_TO_CALLBACK.get(prefix);
    }
}
