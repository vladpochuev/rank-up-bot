package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import com.luxusxc.rank_up.telegram.InlineKeyboardConstructor;
import com.luxusxc.rank_up.telegram.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ChatLIstCommandTest {
    private UserRepository userRepository;
    private ChatRepository chatRepository;
    private TelegramBot bot;
    private Command chatListCommand;

    @BeforeEach
    void init() {
        userRepository = mock();
        chatRepository = mock();
        bot = mock();
        chatListCommand = new ChatListCommand(bot);

        when(bot.getUserRepository()).thenReturn(userRepository);
        when(bot.getChatRepository()).thenReturn(chatRepository);
        when(bot.getKeyboardConstructor()).thenReturn(new InlineKeyboardConstructor());
    }

    @Test
    void testNoChats() {
        Message message = new Message();
        message.setChat(new Chat(1L, "private"));
        message.setFrom(new User(1L, "", false));
        chatListCommand.execute(message);
        verify(bot).sendMessage(message.getChatId(), "You have no rank in any chat.");
    }

    @Test
    void testWithChat() {
        UserEntity user = new UserEntity(new ChatUserId(-1L, 1L));
        List<UserEntity> users = List.of(user);

        ChatEntity chat = new ChatEntity();
        chat.setTitle("TEST");

        when(userRepository.findAll(1L)).thenReturn(users);
        when(chatRepository.findById(-1L)).thenReturn(Optional.of(chat));

        Message message = new Message();
        message.setChat(new Chat(-1L, "group"));
        message.setFrom(new User(1L, "", false));

        chatListCommand.execute(message);
        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).sendMessage(captor.capture());

        SendMessage response = captor.getValue();
        InlineKeyboardMarkup replyMarkup = (InlineKeyboardMarkup) response.getReplyMarkup();
        InlineKeyboardButton button = replyMarkup.getKeyboard().get(0).get(0);

        assertThat(response.getText(), equalTo("Choose the chat."));
        assertThat(button.getText(), equalTo(chat.getTitle()));
    }
}
