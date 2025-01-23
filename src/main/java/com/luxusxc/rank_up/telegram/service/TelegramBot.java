package com.luxusxc.rank_up.telegram.service;

import com.luxusxc.rank_up.telegram.config.BotConfig;
import com.luxusxc.rank_up.telegram.mapper.CallbackQueryMapper;
import com.luxusxc.rank_up.telegram.model.BotAction;
import com.luxusxc.rank_up.common.model.LogTags;
import com.luxusxc.rank_up.telegram.repository.ChatRepository;
import com.luxusxc.rank_up.common.repository.RankRepository;
import com.luxusxc.rank_up.telegram.repository.UserRepository;
import com.luxusxc.rank_up.telegram.command.CommandType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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
    private static final String MESSAGE_SENT_LOG_TEMPLATE = "Message was sent to the chat (id=%s)";
    private static final String MESSAGE_DELETED_LOG_TEMPLATE = "Message was deleted from the chat (id=%s)";
    private static final String ERROR_SENDING_LOG_TEMPLATE = "Error occurred while sending the message: %s";
    private static final String ERROR_DELETING_LOG_TEMPLATE = "Error occurred while deleting the message: %s";
    private static final String ERROR_BOT_COMMANDS_LOG_TEMPLATE = "Error setting bot`s command list: %s";

    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.BOT_SERVICE);

    private final BotConfig config;
    private final StatsMessageFormatter statsMessageFormatter;
    private final DecisionCenter decisionCenter;
    private final InlineKeyboardConstructor keyboardConstructor;
    private final CommandParser commandParser;
    private final CallbackParser callbackParser;
    private final CallbackQueryMapper callbackQueryMapper;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final RankRepository rankRepository;

    public TelegramBot(BotConfig config, StatsMessageFormatter statsMessageFormatter, DecisionCenter decisionCenter,
                       InlineKeyboardConstructor keyboardConstructor, CommandParser commandParser,
                       CallbackParser callbackParser, CallbackQueryMapper callbackQueryMapper,
                       ChatRepository chatRepository, UserRepository userRepository, RankRepository rankRepository) {
        super(config.getToken());
        this.config = config;
        this.statsMessageFormatter = statsMessageFormatter;
        this.decisionCenter = decisionCenter;
        this.keyboardConstructor = keyboardConstructor;
        this.commandParser = commandParser;
        this.callbackParser = callbackParser;
        this.callbackQueryMapper = callbackQueryMapper;
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
            log.error(LOG_MARKER, ERROR_BOT_COMMANDS_LOG_TEMPLATE.formatted(e.getMessage()));
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
            log.info(LOG_MARKER, MESSAGE_SENT_LOG_TEMPLATE.formatted(message.getChatId()));
        } catch (TelegramApiException e) {
            log.error(LOG_MARKER, ERROR_SENDING_LOG_TEMPLATE.formatted(e.getMessage()));
        }
    }

    public void sendMessage(EditMessageText message) {
        try {
            execute(message);
            log.info(LOG_MARKER, MESSAGE_SENT_LOG_TEMPLATE.formatted(message.getChatId()));
        } catch (TelegramApiException e) {
            log.error(LOG_MARKER, ERROR_SENDING_LOG_TEMPLATE.formatted(e.getMessage()));
        }
    }

    public void sendPhoto(SendPhoto photo) {
        try {
            execute(photo);
            log.info(LOG_MARKER, MESSAGE_SENT_LOG_TEMPLATE.formatted(photo.getChatId()));
        } catch (TelegramApiException e) {
            log.error(LOG_MARKER, ERROR_SENDING_LOG_TEMPLATE.formatted(e.getMessage()));
        }
    }

    public void deleteMessage(long chatId, int messageId) {
        DeleteMessage message = new DeleteMessage();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        deleteMessage(message);
    }

    public void deleteMessage(DeleteMessage message) {
        try {
            execute(message);
            log.info(LOG_MARKER, MESSAGE_DELETED_LOG_TEMPLATE.formatted(message.getChatId()));
        } catch (TelegramApiException e) {
            log.error(LOG_MARKER, ERROR_DELETING_LOG_TEMPLATE.formatted(e.getMessage()));
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
