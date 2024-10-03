package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
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
    private final TelegramBot bot;

    public BotLeftChatProcessorTest() {
        chatRepository = mock();
        bot = mock();
        botLeftChatProcessor = new BotLeftChatProcessor(chatRepository);
    }

    @Test
    void testProcessLeaveSupergroup() {
        ChatMemberUpdated memberUpdated = new ChatMemberUpdated();
        Chat chat = new Chat(-2L, "supergroup");
        memberUpdated.setChat(chat);

        botLeftChatProcessor.processLeave(memberUpdated).run(bot);

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

        botLeftChatProcessor.processLeave(memberUpdated).run(bot);

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
}
