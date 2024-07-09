package com.luxusxc.mirror_tg_ds.status;

import com.luxusxc.mirror_tg_ds.commands.Command;
import com.luxusxc.mirror_tg_ds.service.TelegramBot;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StatusFactory {
    private final TelegramBot bot;

    public Command getStatus(StatusType status) {
        switch (status) {
            case DEFAULT -> {

            }
        }
        return null;
    }
}
