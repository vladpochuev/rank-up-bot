package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.StatsMessage;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.service.StatsMessageFormatter;
import com.luxusxc.rank_up.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public class StatsCommand extends Command {
    private final StatsMessageFormatter messageFormatter;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final RankRepository rankRepository;

    public StatsCommand(TelegramBot bot) {
        super(bot);
        messageFormatter = bot.getStatsMessageFormatter();
        chatRepository = bot.getChatRepository();
        userRepository = bot.getUserRepository();
        rankRepository = bot.getRankRepository();
    }

    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();
        StatsMessage statsMessage = buildStatsMessage(chatId);
        String formattedMessage = messageFormatter.getFormattedMessage(statsMessage);
        bot.sendMessage(chatId, formattedMessage, "HTML");
    }

    private StatsMessage buildStatsMessage(long chatId) {
        ChatEntity chat = chatRepository.findById(chatId).orElseThrow();
        UserEntity highestRankUser = userRepository.findHighestLevelUser().orElseThrow();
        RankEntity highestRank = rankRepository.findById(highestRankUser.getRankLevel()).orElseThrow();
        int activeMembers = ((List<UserEntity>) userRepository.findALlByChatId(chatId)).size();

        return new StatsMessage(chat, highestRank, highestRankUser, activeMembers);
    }
}
