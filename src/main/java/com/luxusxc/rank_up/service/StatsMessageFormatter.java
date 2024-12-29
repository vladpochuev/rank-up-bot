package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.StatsMessage;
import com.luxusxc.rank_up.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class StatsMessageFormatter {
    private final MessageFormatter messageFormatter;
    private static final String MESSAGE_TEMPLATE = """
            <code>
            Group name:	%s
            Bot active since:	%s
            Active members:	%s
            Highest level:	%s
            Highest rank name:	%s
            Highest rank user:	@%s
            </code>
            """;

    private static final String DATE_TEMPLATE = "dd.MM.yyyy";
    private static final String HIGHEST_LEVEL_MESSAGE_TEMPLATE = "%s level, %s exp";

    public String getFormattedMessage(StatsMessage statsMessage) {
        String message = MESSAGE_TEMPLATE.formatted(
                statsMessage.getChat().getTitle(),
                getFormattedDate(statsMessage.getChat().getDate()),
                statsMessage.getActiveMembers(),
                getLevelMessage(statsMessage.getHighestRankUser()),
                statsMessage.getHighestRank().getName(),
                statsMessage.getHighestRankUser().getUserName());

        return messageFormatter.alignMessage(message, 40);
    }

    private String getFormattedDate(int unix) {
        LocalDateTime date = LocalDateTime.ofEpochSecond(unix, 0, ZonedDateTime.now().getOffset());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TEMPLATE);
        return formatter.format(date);
    }

    private String getLevelMessage(UserEntity user) {
        return HIGHEST_LEVEL_MESSAGE_TEMPLATE.formatted(user.getRankLevel(), user.getExperience());
    }
}
