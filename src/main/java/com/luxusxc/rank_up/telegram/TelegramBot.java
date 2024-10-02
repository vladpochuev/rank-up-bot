package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.config.BotConfig;
import com.luxusxc.rank_up.telegram.commands.CommandType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final DecisionCenter decisionCenter;

    public TelegramBot(BotConfig config, DecisionCenter decisionCenter) {
        super(config.getToken());
        this.config = config;
        this.decisionCenter = decisionCenter;
    }

    @Override
    public void onUpdateReceived(Update update) {
        setBotCommands();
        BotAction action = decisionCenter.processUpdate(update);
        if (action != null) {
            action.run(this);
        }
    }

    public void setBotCommands() {
        try {
            List<BotCommand> commands = extractCommands();
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot`s command list " + e.getMessage());
        }
    }

    private List<BotCommand> extractCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        for (CommandType command : CommandType.values()) {
            listOfCommands.add(new BotCommand(command.body, command.description));
        }
        return listOfCommands;
    }

    public void sendMessage(long chatId, String textToSend) {
        sendMessage(chatId, textToSend, null);
    }

    public void sendMessage(long chatId, String textToSend, String parseMode) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setParseMode(parseMode);
        sendMessage(message);
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}