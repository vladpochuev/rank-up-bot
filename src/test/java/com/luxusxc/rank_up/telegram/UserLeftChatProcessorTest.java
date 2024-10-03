package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberLeft;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UserLeftChatProcessorTest {
    private final UserLeftChatProcessor userLeftChatProcessor;
    private final UserRepository userRepository;
    private final TelegramBot bot;

    public UserLeftChatProcessorTest() {
        userRepository = mock();
        bot = mock();
        userLeftChatProcessor = new UserLeftChatProcessor(userRepository);
    }

    @Test
    void testProcessLeave() {
        User user = new User(-1L, "Name", false);
        ChatMemberUpdated memberUpdated = new ChatMemberUpdated();
        memberUpdated.setOldChatMember(new ChatMemberMember(user));
        memberUpdated.setNewChatMember(new ChatMemberLeft(user));
        Chat chat = new Chat(-2L, "supergroup");
        memberUpdated.setChat(chat);

        userLeftChatProcessor.processLeave(memberUpdated).run(bot);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).delete(captor.capture());

        UserEntity entity = captor.getValue();
        ChatUserId chatUserId = entity.getChatUserId();

        assertThat(user.getId(), equalTo(chatUserId.getUserId()));
        assertThat(chat.getId(), equalTo(chatUserId.getChatId()));
    }
}
