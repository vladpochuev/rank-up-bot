package com.luxusxc.rank_up.telegram.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InlineKeyboardConstructor {
    public InlineKeyboardMarkup getInlineKeyboard(List<Map<String, String>> values) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (Map<String, String> map : values) {
            rowsInline.add(getInlineKeyboardRow(map));
        }
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private List<InlineKeyboardButton> getInlineKeyboardRow(Map<String, String> map) {
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (Map.Entry<String, String> entrySet : map.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setCallbackData(entrySet.getKey());
            button.setText(entrySet.getValue());
            rowInline.add(button);
        }
        return rowInline;
    }
}
