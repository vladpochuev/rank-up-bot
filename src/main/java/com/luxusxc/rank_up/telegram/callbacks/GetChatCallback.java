package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class GetChatCallback extends Callback {
    private static final String MESSAGE_FORMAT = "Rank: %s\nLevel: %d\nExperience: %d\n";

    public GetChatCallback(TelegramBot bot) {
        super(bot);
    }

    @Override
    public void execute(CallbackQuery query) {
        String message = query.getData();
        long chatId = Long.parseLong(message.substring(CallbackType.GET_CHAT.prefix.length() + 1));
        long userId = query.getFrom().getId();

        UserEntity user = getUser(new ChatUserId(chatId, userId));
        RankEntity rank = getUserRank(user);

        String sendMessage = MESSAGE_FORMAT.formatted(rank.getName(), user.getRankLevel(), user.getExperience());
        sendEditMessage(query, sendMessage);
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
