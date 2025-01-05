package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Slf4j
public class GetChatCallback extends Callback {
    private static final String MESSAGE_TEMPLATE = "Rank: %s\nLevel: %d\nExperience: %d\n";
    private static final String SUCCESS_LOG_TEMPLATE = "User has received his statistics in the chat (%s)";

    protected static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CALLBACK);

    public GetChatCallback(TelegramBot bot) {
        super(bot);
    }

    @Override
    public void executeCallback(CallbackQuery query) {
        String message = query.getData();
        long chatId = Long.parseLong(message.substring(CallbackType.GET_CHAT.prefix.length() + 1));
        long userId = query.getFrom().getId();

        UserEntity user = getUser(new ChatUserId(chatId, userId));
        RankEntity rank = getUserRank(user);

        String sendMessage = MESSAGE_TEMPLATE.formatted(rank.getName(), user.getRankLevel(), user.getExperience());
        sendEditMessage(query, sendMessage);
        log.info(LOG_MARKER, SUCCESS_LOG_TEMPLATE.formatted(user.getChatUserId()));
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
        bot.sendMessage(response);
    }
}
