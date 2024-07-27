package com.luxusxc.rank_up.model;

import java.util.function.Function;

public enum Variable {
    NEW_LEVEL("{newlvl}", (o) -> String.valueOf(((RankEntity)o).getLevel())),
    NEW_RANK("{newrank}", (o) -> ((RankEntity)o).getRank()),
    NAME("{name}", (o) -> ((UserEntity)o).getName());
    private final String tag;
    private final Function<Object, String> callback;

    Variable(String tag, Function<Object, String> callback) {
        this.tag = tag;
        this.callback = callback;
    }

    public String getTag() {
        if (tag.startsWith("{")) return "\\" + tag;
        return tag;
    }

    public Function<Object, String> getCallback() {
        return callback;
    }
}
