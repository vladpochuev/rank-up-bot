package com.luxusxc.rank_up.telegram.status;

import com.luxusxc.rank_up.telegram.commands.Command;
import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StatusFactory {
    private final TelegramBot bot;

    public Command getStatus(StatusType status) {
        if (status == null) throw new IllegalArgumentException();
        switch (status) {
            case DEFAULT -> {}
        }
        return null;
    }
}
