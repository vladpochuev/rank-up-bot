package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.model.*;
import com.luxusxc.rank_up.repository.ChatRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.service.CallbackParser;
import com.luxusxc.rank_up.telegram.InlineKeyboardConstructor;
import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class GetChatCallback extends Callback {
    private static final String MESSAGE_TEMPLATE = """
            <b>%s</b>
            
            Rank: %s
            Level: %d
            Experience: %d
            """;
    private static final String SUCCESS_LOG_TEMPLATE = "User has received his statistics in the chat (%s)";
    private static final String BACK_CALLBACK = "BACK";
    private static final String BACK_BUTTON_NAME = "Back";

    protected static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CALLBACK);

    public GetChatCallback(TelegramBot bot) {
        super(bot);
    }

    @Override
    public void executeCallback(CallbackQuery query) {
        String message = query.getData();
        long chatId = getChatIdFromCallback(message);
        long userId = query.getFrom().getId();

        ChatEntity chat = getChat(chatId);
        UserEntity user = getUser(new ChatUserId(chatId, userId));
        RankEntity rank = getUserRank(user);

        String sendMessage = MESSAGE_TEMPLATE.formatted(chat.getTitle(), rank.getName(), user.getRankLevel(), user.getExperience());
        sendEditMessage(query, sendMessage);
        log.info(LOG_MARKER, SUCCESS_LOG_TEMPLATE.formatted(user.getChatUserId()));
    }

    private long getChatIdFromCallback(String message) {
        CallbackParser callbackParser = bot.getCallbackParser();
        List<String> args = callbackParser.getArgs(message);
        return Long.parseLong(args.get(0));
    }

    private ChatEntity getChat(long chatId) {
        ChatRepository chatRepository = bot.getChatRepository();
        return chatRepository.findById(chatId).orElseThrow();
    }

    private UserEntity getUser(ChatUserId chatUserId) {
        UserRepository userRepository = bot.getUserRepository();
        return userRepository.findById(chatUserId).orElseThrow();
    }

    private RankEntity getUserRank(UserEntity user) {
        RankRepository rankRepository = bot.getRankRepository();
        return rankRepository.findById(user.getRankLevel()).orElseThrow();
    }

    private void sendEditMessage(CallbackQuery callbackQuery, String message) {
        EditMessageText response = new EditMessageText();
        response.setChatId(callbackQuery.getMessage().getChatId());
        response.setMessageId(callbackQuery.getMessage().getMessageId());
        response.setText(message);
        response.setReplyMarkup(getBackMarkup());
        response.setParseMode("HTML");
        bot.sendMessage(response);
    }

    private InlineKeyboardMarkup getBackMarkup() {
        InlineKeyboardConstructor constructor = bot.getKeyboardConstructor();
        List<Map<String, String>> rows = new ArrayList<>();
        rows.add(Map.of(BACK_CALLBACK, BACK_BUTTON_NAME));
        return constructor.getInlineKeyboard(rows);
    }
}
