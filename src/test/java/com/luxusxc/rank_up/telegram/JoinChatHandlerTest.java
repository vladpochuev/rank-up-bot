package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class JoinChatHandlerTest {
    private final JoinChatHandler joinChatHandler;
    private final ChatRepository chatRepository;

    public JoinChatHandlerTest() {
        chatRepository = mock();
        joinChatHandler = new JoinChatHandler(chatRepository);
    }

    @Test
    void testUpdateChat() {
        ChatMember oldMember = new ChatMemberMember();
        ChatMember newMember = new ChatMemberAdministrator();
        Chat chat = new Chat(-1L, "supergroup");
        chat.setTitle("Test");

        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setOldChatMember(oldMember);
        chatMemberUpdated.setNewChatMember(newMember);
        chatMemberUpdated.setChat(chat);
        chatMemberUpdated.setDate(1);

        joinChatHandler.updateChatInfo(chatMemberUpdated);
        ArgumentCaptor<ChatEntity> captor = ArgumentCaptor.forClass(ChatEntity.class);
        verify(chatRepository, times(1)).save(captor.capture());

        ChatEntity chatEntity = captor.getValue();
        assertThat(chatEntity.getId(), equalTo(chat.getId()));
        assertThat(chatEntity.getTitle(), equalTo(chat.getTitle()));
        assertThat(chatEntity.getDate(), equalTo(chatMemberUpdated.getDate()));
    }

    @Test
    void testUpdateChatWrong() {
        ChatMember oldMember = new ChatMemberAdministrator();
        ChatMember newMember = new ChatMemberAdministrator();
        Chat chat = new Chat(-1L, "supergroup");
        chat.setTitle("Test");

        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setOldChatMember(oldMember);
        chatMemberUpdated.setNewChatMember(newMember);
        chatMemberUpdated.setChat(chat);

        joinChatHandler.updateChatInfo(chatMemberUpdated);
        verify(chatRepository, times(0)).save(any());
    }

    @Test
    void testUpdateChatChannel() {
        ChatMember oldMember = new ChatMemberMember();
        ChatMember newMember = new ChatMemberAdministrator();
        Chat chat = new Chat(-1L, "channel");
        chat.setTitle("Test");

        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setOldChatMember(oldMember);
        chatMemberUpdated.setNewChatMember(newMember);
        chatMemberUpdated.setChat(chat);

        joinChatHandler.updateChatInfo(chatMemberUpdated);
        verify(chatRepository, times(0)).save(any());
    }
}
