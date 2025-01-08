package com.luxusxc.rank_up.telegram.callbacks;

import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.service.CallbackParser;
import com.luxusxc.rank_up.service.StringSplitter;
import com.luxusxc.rank_up.telegram.InlineKeyboardConstructor;
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
    private ChatRepository chatRepository;

    private TelegramBot bot;
    private GetChatCallback callback;
    private final CallbackParser callbackParser = new CallbackParser(new StringSplitter());
    private final InlineKeyboardConstructor inlineKeyboardConstructor = new InlineKeyboardConstructor();

    @BeforeEach
    void init() {
        userRepository = mock();
        rankRepository = mock();
        chatRepository = mock();
        bot = mock();
        callback = new GetChatCallback(bot);

        when(bot.getUserRepository()).thenReturn(userRepository);
        when(bot.getRankRepository()).thenReturn(rankRepository);
        when(bot.getChatRepository()).thenReturn(chatRepository);
        when(bot.getCallbackParser()).thenReturn(callbackParser);
        when(bot.getKeyboardConstructor()).thenReturn(inlineKeyboardConstructor);
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

        ChatEntity chat = new ChatEntity(-1L, "Title", 1736280351);
        when(chatRepository.findById(-1L)).thenReturn(Optional.of(chat));

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
        assertThat(actual.getText(), equalTo("<b>Title</b>\n\nRank: HERALD\nLevel: 1\nExperience: 2\n"));
    }
}
