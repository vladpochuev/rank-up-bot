package com.luxusxc.rank_up.mapper;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


class CallbackQueryMapperTest {
    private final CallbackQueryMapper mapper = CallbackQueryMapper.INSTANCE;

    @Test
    void testToMessage() {
        CallbackQuery query = new CallbackQuery();
        query.setFrom(new User(1L, "firstName", false));
        Message message = mapper.toMessage(query);

        assertThat(message.getFrom().getId(), equalTo(1L));
        assertThat(message.getFrom().getFirstName(), equalTo("firstName"));
        assertThat(message.getFrom().getIsBot(), equalTo(false));
    }
}