package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.config.BotConfig;
import com.luxusxc.rank_up.model.BotAction;
import com.luxusxc.rank_up.repository.ChatRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.service.StatsMessageFormatter;
import com.luxusxc.rank_up.telegram.commands.CommandType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllGroupChats;
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
    private final StatsMessageFormatter statsMessageFormatter;
    private final DecisionCenter decisionCenter;
    private final InlineKeyboardConstructor keyboardConstructor;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final RankRepository rankRepository;

    public TelegramBot(BotConfig config, StatsMessageFormatter statsMessageFormatter, DecisionCenter decisionCenter, InlineKeyboardConstructor keyboardConstructor, ChatRepository chatRepository, UserRepository userRepository, RankRepository rankRepository) {
        super(config.getToken());
        this.config = config;
        this.statsMessageFormatter = statsMessageFormatter;
        this.decisionCenter = decisionCenter;
        this.keyboardConstructor = keyboardConstructor;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        setBotCommands();
        BotAction action = decisionCenter.processUpdate(update);
        if (action != null) {
            action.execute(this);
        }
    }

    public void setBotCommands() {
        try {
            List<BotCommand> userCommands = extractUserCommands();
            List<BotCommand> groupCommands = extractGroupCommands();
            execute(new SetMyCommands(userCommands, new BotCommandScopeDefault(), null));
            execute(new SetMyCommands(groupCommands, new BotCommandScopeAllGroupChats(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot`s command list " + e.getMessage());
        }
    }

    private List<BotCommand> extractUserCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        for (CommandType command : CommandType.getUserCommands()) {
            listOfCommands.add(new BotCommand(command.body, command.description));
        }
        return listOfCommands;
    }

    private List<BotCommand> extractGroupCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        for (CommandType command : CommandType.getGroupCommands()) {
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

    public void sendMessage(EditMessageText message) {
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