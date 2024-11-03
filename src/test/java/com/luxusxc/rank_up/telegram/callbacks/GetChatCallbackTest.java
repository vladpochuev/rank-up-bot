package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.telegram.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class GetChatCallbackTest {
    private UserRepository userRepository;
    private RankRepository rankRepository;
    private TelegramBot bot;
    private GetChatCallback callback;

    @BeforeEach
    void init() {
        userRepository = mock();
        rankRepository = mock();
        bot = mock();
        callback = new GetChatCallback(bot);

        when(bot.getUserRepository()).thenReturn(userRepository);
        when(bot.getRankRepository()).thenReturn(rankRepository);
    }

    @Test
    void testChat() {
        ChatUserId chatUserId = new ChatUserId(-1L, 1L);
        UserEntity userEntity = new UserEntity(chatUserId);
        userEntity.setRankLevel(1);
        userEntity.setExperience(2L);

        when(userRepository.findById(chatUserId)).thenReturn(Optional.of(userEntity));

        RankEntity rank = new RankEntity("HERALD", 5L, "");
        when(rankRepository.findById(1)).thenReturn(Optional.of(rank));

        CallbackQuery query = new CallbackQuery();
        query.setData("CHAT_-1");
        query.setFrom(new User(chatUserId.getUserId(), "", false));

        Message message = new Message();
        message.setMessageId(0);
        message.setChat(new Chat(chatUserId.getChatId(), "user"));

        query.setMessage(message);

        callback.execute(query);

        ArgumentCaptor<EditMessageText> captor = ArgumentCaptor.forClass(EditMessageText.class);
        verify(bot).sendMessage(captor.capture());

        EditMessageText actual = captor.getValue();
        assertThat(actual.getText(), equalTo("Rank: HERALD\nLevel: 1\nExperience: 2\n"));
    }
}
