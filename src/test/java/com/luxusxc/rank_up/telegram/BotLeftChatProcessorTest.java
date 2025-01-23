package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.telegram.model.BotAction;
import com.luxusxc.rank_up.telegram.model.ChatEntity;
import com.luxusxc.rank_up.telegram.repository.ChatRepository;
import com.luxusxc.rank_up.telegram.repository.UserRepository;
import com.luxusxc.rank_up.telegram.service.BotLeftChatProcessor;
import com.luxusxc.rank_up.telegram.service.TelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class BotLeftChatProcessorTest {
    private final BotLeftChatProcessor botLeftChatProcessor;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final TelegramBot bot;

    public BotLeftChatProcessorTest() {
        chatRepository = mock();
        userRepository = mock();
        bot = mock();
        botLeftChatProcessor = new BotLeftChatProcessor(chatRepository, userRepository);
    }

    @Test
    void testProcessLeaveSupergroup() {
        ChatMemberUpdated memberUpdated = new ChatMemberUpdated();
        Chat chat = new Chat(-2L, "supergroup");
        memberUpdated.setChat(chat);

        botLeftChatProcessor.processLeave(memberUpdated).execute(bot);

        ArgumentCaptor<ChatEntity> captor = ArgumentCaptor.forClass(ChatEntity.class);
        verify(chatRepository).delete(captor.capture());

        ChatEntity chatEntity = captor.getValue();
        assertThat(chatEntity.getId(), equalTo(chat.getId()));
    }

    @Test
    void testProcessLeaveGroup() {
        ChatMemberUpdated memberUpdated = new ChatMemberUpdated();
        Chat chat = new Chat(-2L, "group");
        memberUpdated.setChat(chat);

        botLeftChatProcessor.processLeave(memberUpdated).execute(bot);

        ArgumentCaptor<ChatEntity> captor = ArgumentCaptor.forClass(ChatEntity.class);
        verify(chatRepository).delete(captor.capture());

        ChatEntity chatEntity = captor.getValue();
        assertThat(chatEntity.getId(), equalTo(chat.getId()));
    }

    @Test
    void testProcessLeaveUser() {
        ChatMemberUpdated memberUpdated = new ChatMemberUpdated();
        Chat chat = new Chat(-2L, "user");
        memberUpdated.setChat(chat);

        BotAction action = botLeftChatProcessor.processLeave(memberUpdated);
        assertThat(action, nullValue());
    }

    @Test
    void testProcessLeaveGroupDeleteChatUsers() {
        ChatMemberUpdated memberUpdated = new ChatMemberUpdated();
        Chat chat = new Chat(-2L, "group");
        memberUpdated.setChat(chat);

        botLeftChatProcessor.processLeave(memberUpdated).execute(bot);
        verify(userRepository, times(1)).deleteAllChatUsers(chat.getId());
    }
}
