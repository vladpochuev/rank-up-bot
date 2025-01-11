package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.model.ImageEntity;
import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.UserEntity;
import com.luxusxc.rank_up.repository.ImageRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.service.VariableReplacer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LevelUpAnnouncer {
    private static final String SEND_TEXT_LOG_TEMPLATE = "The new level congratulation was sent to the group (%s)";
    private static final String SEND_IMAGE_LOG_TEMPLATE = "The new level congratulation with an attached image was sent to the group (%s)";
    private static final String SEND_IMAGE_GROUP_LOG_TEMPLATE = "The new level congratulation with several attached images was sent to the group (%s)";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.BOT_SERVICE);

    private final RankRepository rankRepository;
    private final VariableReplacer variableReplacer;
    private final ImageRepository imageRepository;

    public void announce(TelegramBot bot, UserEntity user) {
        List<ImageEntity> images = (List<ImageEntity>) imageRepository.findAll();

        switch (images.size()) {
            case 0 -> sendText(user, bot);
            case 1 -> sendImage(images.get(0), user, bot);
            default -> sendImageGroup(images, user, bot);
        }
    }

    private void sendText(UserEntity user, TelegramBot bot) {
        long chatId = user.getChatUserId().getChatId();
        String message = getMessage(user);

        bot.sendMessage(chatId, message);
        log.info(LOG_MARKER, SEND_TEXT_LOG_TEMPLATE.formatted(user.getChatUserId()));
    }

    private void sendImage(ImageEntity image, UserEntity user, TelegramBot bot) {
        long chatId = user.getChatUserId().getChatId();
        String message = getMessage(user);

        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        photo.setCaption(message);
        photo.setPhoto(new InputFile(image.getImageUrl()));

        bot.sendPhoto(photo);
        log.info(LOG_MARKER, SEND_IMAGE_LOG_TEMPLATE.formatted(user.getChatUserId()));
    }

    private void sendImageGroup(List<ImageEntity> images, UserEntity user, TelegramBot bot) {
        long chatId = user.getChatUserId().getChatId();
        String message = getMessage(user);
        List<InputMedia> photos = createPhotos(images);

        SendMediaGroup mediaGroup = new SendMediaGroup();
        mediaGroup.setChatId(chatId);
        mediaGroup.setMedias(photos);
        photos.get(0).setCaption(message);

        bot.sendMediaGroup(mediaGroup);
        log.info(LOG_MARKER, SEND_IMAGE_GROUP_LOG_TEMPLATE.formatted(user.getChatUserId()));
    }

    private List<InputMedia> createPhotos(List<ImageEntity> images) {
        List<InputMedia> inputMediaPhotos = new ArrayList<>();
        for (ImageEntity image : images) {
            InputMediaPhoto photo = new InputMediaPhoto();
            photo.setMedia(image.getImageUrl());
            inputMediaPhotos.add(photo);
        }
        return inputMediaPhotos;
    }

    private String getMessage(UserEntity user) {
        RankEntity newRank = rankRepository.findById(user.getRankLevel()).orElseThrow();
        String message = newRank.getLevelUpMessage();
        return variableReplacer.replaceUserVars(message, user);
    }
}
