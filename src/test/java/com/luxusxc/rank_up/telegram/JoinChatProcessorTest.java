package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ChatEntity;
import com.luxusxc.rank_up.repository.ChatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.chatmember.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class JoinChatProcessorTest {
    private final JoinChatProcessor joinChatProcessor;
    private final ChatRepository chatRepository;
    private final TelegramBot bot;

    public JoinChatProcessorTest() {
        chatRepository = mock();
        bot = mock();
        joinChatProcessor = new JoinChatProcessor(chatRepository);
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

        joinChatProcessor.updateChatInfo(chatMemberUpdated).run(bot);
        ArgumentCaptor<ChatEntity> captor = ArgumentCaptor.forClass(ChatEntity.class);
        verify(chatRepository, times(1)).save(captor.capture());

        ChatEntity chatEntity = captor.getValue();
        assertThat(chatEntity.getId(), equalTo(chat.getId()));
        assertThat(chatEntity.getTitle(), equalTo(chat.getTitle()));
        assertThat(chatEntity.getDate(), equalTo(chatMemberUpdated.getDate()));
    }

    @Test
    void testUpdateChatKicked() {
        ChatMember oldMember = new ChatMemberBanned();
        ChatMember newMember = new ChatMemberAdministrator();
        Chat chat = new Chat(-1L, "supergroup");
        chat.setTitle("Test");

        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setOldChatMember(oldMember);
        chatMemberUpdated.setNewChatMember(newMember);
        chatMemberUpdated.setChat(chat);
        chatMemberUpdated.setDate(1);

        joinChatProcessor.updateChatInfo(chatMemberUpdated).run(bot);
        ArgumentCaptor<ChatEntity> captor = ArgumentCaptor.forClass(ChatEntity.class);
        verify(chatRepository, times(1)).save(captor.capture());

        ChatEntity chatEntity = captor.getValue();
        assertThat(chatEntity.getId(), equalTo(chat.getId()));
        assertThat(chatEntity.getTitle(), equalTo(chat.getTitle()));
        assertThat(chatEntity.getDate(), equalTo(chatMemberUpdated.getDate()));
    }

    @Test
    void testUpdateChatLeft() {
        ChatMember oldMember = new ChatMemberLeft();
        ChatMember newMember = new ChatMemberAdministrator();
        Chat chat = new Chat(-1L, "supergroup");
        chat.setTitle("Test");

        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setOldChatMember(oldMember);
        chatMemberUpdated.setNewChatMember(newMember);
        chatMemberUpdated.setChat(chat);
        chatMemberUpdated.setDate(1);

        joinChatProcessor.updateChatInfo(chatMemberUpdated).run(bot);
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

        joinChatProcessor.updateChatInfo(chatMemberUpdated);
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

        joinChatProcessor.updateChatInfo(chatMemberUpdated);
        verify(chatRepository, times(0)).save(any());
    }
}
