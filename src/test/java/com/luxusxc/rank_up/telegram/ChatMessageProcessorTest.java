package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChatMessageProcessorTest {
    private final UserRepository userRepository;
    private final RankRepository rankRepository;
    private final ChatMessageProcessor chatMessageProcessor;

    public ChatMessageProcessorTest() {
        userRepository = mock();
        rankRepository = mock();
        chatMessageProcessor = new ChatMessageProcessor(userRepository, rankRepository);
    }

    @BeforeEach
    void init() {
        RankEntity rank1 = new RankEntity(1, "HEROLD", 20L, "message");
        RankEntity rank2 = new RankEntity(2, "GUARDIAN", 40L, "message");
        when(rankRepository.findById(1)).thenReturn(Optional.of(rank1));
        when(rankRepository.findById(2)).thenReturn(Optional.of(rank2));
    }

    @Test
    void testProcessMessageNewUser() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        Message message = new Message();
        message.setChat(new Chat(-1L, "supergroup"));
        message.setFrom(new User(-2L, "", false));

        chatMessageProcessor.processMessage(message);
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());

        UserEntity userEntity = captor.getValue();
        assertThat(userEntity.getChatUserId().getChatId(), equalTo(-1L));
        assertThat(userEntity.getChatUserId().getUserId(), equalTo(-2L));
        assertThat(userEntity.getRankLevel(), equalTo(1));
        assertThat(userEntity.getExperience(), equalTo(1L));
    }

    @Test
    void testProcessMessage() {
        UserEntity user = new UserEntity(new ChatUserId(-1L, -1L));
        user.setRankLevel(1);
        user.setExperience(10L);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        Message message = new Message();
        message.setChat(new Chat(user.getChatUserId().getChatId(), "supergroup"));
        message.setFrom(new User(user.getChatUserId().getUserId(), "", false));

        chatMessageProcessor.processMessage(message);
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());

        UserEntity actual = captor.getValue();
        assertThat(actual.getExperience(), equalTo(11L));
    }

    @Test
    void testProcessMessageNewLevel() {
        UserEntity user = new UserEntity(new ChatUserId(-1L, -1L));
        user.setRankLevel(1);
        user.setExperience(20L);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        Message message = new Message();
        message.setChat(new Chat(user.getChatUserId().getChatId(), "supergroup"));
        message.setFrom(new User(user.getChatUserId().getUserId(), "", false));

        chatMessageProcessor.processMessage(message);
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());

        UserEntity actual = captor.getValue();
        assertThat(actual.getRankLevel(), equalTo(2));
        assertThat(actual.getExperience(), equalTo(1L));
    }

    @Test
    void testProcessMessageMaxLevelAndExperience() {
        UserEntity user = new UserEntity(new ChatUserId(-1L, -1L));
        user.setRankLevel(2);
        user.setExperience(40L);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        Message message = new Message();
        message.setChat(new Chat(user.getChatUserId().getChatId(), "supergroup"));
        message.setFrom(new User(user.getChatUserId().getUserId(), "", false));

        chatMessageProcessor.processMessage(message);
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(0)).save(captor.capture());
    }
}
