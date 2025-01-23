package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.telegram.model.ChatUserId;
import com.luxusxc.rank_up.common.model.ImageEntity;
import com.luxusxc.rank_up.common.model.RankEntity;
import com.luxusxc.rank_up.telegram.model.UserEntity;
import com.luxusxc.rank_up.common.repository.ImageRepository;
import com.luxusxc.rank_up.common.repository.RankRepository;
import com.luxusxc.rank_up.common.service.VariableReplacer;
import com.luxusxc.rank_up.telegram.service.LevelUpAnnouncer;
import com.luxusxc.rank_up.telegram.service.TelegramBot;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

class LevelUpAnnouncerTest {
    private final LevelUpAnnouncer announcer;
    private final RankRepository rankRepository;
    private final ImageRepository imageRepository;
    private final TelegramBot bot;

    public LevelUpAnnouncerTest() {
        rankRepository = mock();
        imageRepository = mock();
        bot = mock();
        announcer = new LevelUpAnnouncer(rankRepository, new VariableReplacer(), imageRepository);
    }

    @Test
    void testAnnounceNoImages() {
        UserEntity user = new UserEntity(new ChatUserId(-1L, 1L));
        user.setRankLevel(2);
        when(rankRepository.findById(2)).thenReturn(Optional.of(new RankEntity("RANK", 10L, "Congratulations!")));
        when(imageRepository.findAll()).thenReturn(List.of());
        announcer.announce(bot, user);
        verify(bot, times(1)).sendMessage(-1L, "Congratulations!");
    }

    @Test
    void testAnnounceOneImage() {
        UserEntity user = new UserEntity(new ChatUserId(-1L, 1L));
        user.setRankLevel(2);
        when(rankRepository.findById(2)).thenReturn(Optional.of(new RankEntity("RANK", 10L, "Congratulations!")));
        ImageEntity imageEntity = new ImageEntity("https://i.imgur.com/NnDa91l.jpeg");
        when(imageRepository.findAll()).thenReturn(List.of(imageEntity));
        announcer.announce(bot, user);

        ArgumentCaptor<SendPhoto> argumentCaptor = ArgumentCaptor.forClass(SendPhoto.class);
        verify(bot, times(1)).sendPhoto(argumentCaptor.capture());
        SendPhoto sendPhoto = argumentCaptor.getValue();

        assertThat(sendPhoto.getChatId(), equalTo("-1"));
        assertThat(sendPhoto.getCaption(), equalTo("Congratulations!"));
        assertThat(sendPhoto.getPhoto().getAttachName(), equalTo(imageEntity.getImageUrl()));
    }

    @Test
    void testAnnounceSeveralImages() {
        UserEntity user = new UserEntity(new ChatUserId(-1L, 1L));
        user.setRankLevel(2);
        when(rankRepository.findById(2)).thenReturn(Optional.of(new RankEntity("RANK", 10L, "Congratulations!")));
        ImageEntity imageEntity1 = new ImageEntity("https://i.imgur.com/NnDa91l.jpeg");
        ImageEntity imageEntity2 = new ImageEntity("https://i.imgur.com/UCkvJEB.jpeg");
        when(imageRepository.findAll()).thenReturn(List.of(imageEntity1, imageEntity2));
        announcer.announce(bot, user);

        ArgumentCaptor<SendPhoto> argumentCaptor = ArgumentCaptor.forClass(SendPhoto.class);
        verify(bot, times(1)).sendPhoto(argumentCaptor.capture());
        SendPhoto photo = argumentCaptor.getValue();

        assertThat(photo.getChatId(), equalTo("-1"));
        assertThat(photo.getCaption(), equalTo("Congratulations!"));
        assertThat(photo.getPhoto().getAttachName(), anyOf(equalTo(imageEntity1.getImageUrl()), equalTo(imageEntity2.getImageUrl())));
    }
}