package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.ImageEntity;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.ImageRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.service.VariableReplacer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
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

        ArgumentCaptor<SendMediaGroup> argumentCaptor = ArgumentCaptor.forClass(SendMediaGroup.class);
        verify(bot, times(1)).sendMediaGroup(argumentCaptor.capture());
        SendMediaGroup sendMediaGroup = argumentCaptor.getValue();

        assertThat(sendMediaGroup.getChatId(), equalTo("-1"));
        List<InputMedia> mediaPhotos = sendMediaGroup.getMedias();

        InputMediaPhoto photo1 = (InputMediaPhoto) mediaPhotos.get(0);
        InputMediaPhoto photo2 = (InputMediaPhoto) mediaPhotos.get(1);

        assertThat(photo1.getCaption(), equalTo("Congratulations!"));
        assertThat(photo2.getCaption(), nullValue());

        assertThat(photo1.getMedia(), equalTo(imageEntity1.getImageUrl()));
        assertThat(photo2.getMedia(), equalTo(imageEntity2.getImageUrl()));
    }
}