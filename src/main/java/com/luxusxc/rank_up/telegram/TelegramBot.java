package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.service.Utils;
import com.luxusxc.rank_up.telegram.callbacks.Callback;
import com.luxusxc.rank_up.telegram.callbacks.CallbackFactory;
import com.luxusxc.rank_up.telegram.callbacks.CallbackType;
import com.luxusxc.rank_up.telegram.commands.Command;
import com.luxusxc.rank_up.telegram.commands.CommandFactory;
import com.luxusxc.rank_up.telegram.commands.CommandType;
import com.luxusxc.rank_up.config.BotConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
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

    public TelegramBot(BotConfig config) {
        super(config.getToken());
        this.config = config;
        addListOfCommands();
    }

    private void addListOfCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        for (CommandType command : CommandType.values()) {
            listOfCommands.add(new BotCommand(command.body, command.description));
        }

        try {
            execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot`s command list " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            handleCommand(message);
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            handleCallback(callbackQuery);
        }
    }

    private void handleCommand(Message message) {
        String messageText = message.getText();
        Command command = defineCommand(messageText);
        command.execute(message);
    }

    private Command defineCommand(String messageText) {
        CommandFactory commandFactory = new CommandFactory(this);
        String commandBody = Utils.getCommandBody(messageText);
        CommandType commandType = CommandType.getInstance(commandBody);
        return commandFactory.getCommand(commandType);
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        String value = callbackQuery.getData();
        String prefix = value.split("_")[0];
        Callback callback = defineCallback(prefix);
        callback.execute(callbackQuery);
    }

    private Callback defineCallback(String prefix) {
        CallbackFactory callbackFactory = new CallbackFactory(this);
        CallbackType callbackType = CallbackType.getInstance(prefix);
        return callbackFactory.getCallback(callbackType);
    }

    public void sendMessage(long chatId, String textToSend) {
        sendMessage(chatId, textToSend, null);
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    public void sendMessage(long chatId, String textToSend, String parseMode) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setParseMode(parseMode);
        sendMessage(message);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}