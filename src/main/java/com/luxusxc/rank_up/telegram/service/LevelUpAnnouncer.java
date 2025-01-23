package com.luxusxc.rank_up.telegram.service;

import com.luxusxc.rank_up.common.model.ImageEntity;
import com.luxusxc.rank_up.common.model.LogTags;
import com.luxusxc.rank_up.common.model.RankEntity;
import com.luxusxc.rank_up.telegram.model.UserEntity;
import com.luxusxc.rank_up.common.repository.ImageRepository;
import com.luxusxc.rank_up.common.repository.RankRepository;
import com.luxusxc.rank_up.common.service.VariableReplacer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LevelUpAnnouncer {
    private static final String SEND_TEXT_LOG_TEMPLATE = "The new level congratulation was sent to the group (%s)";
    private static final String SEND_IMAGE_LOG_TEMPLATE = "The new level congratulation with an attached image was sent to the group (%s)";
    private static final String PICKED_IMAGE_LOG_TEMPLATE = "The image (%s) was picked randomly from %d (%s)";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.BOT_SERVICE);

    private final RankRepository rankRepository;
    private final VariableReplacer variableReplacer;
    private final ImageRepository imageRepository;

    public void announce(TelegramBot bot, UserEntity user) {
        List<ImageEntity> images = (List<ImageEntity>) imageRepository.findAll();

        switch (images.size()) {
            case 0 -> sendText(user, bot);
            case 1 -> sendImage(images.get(0), user, bot);
            default -> sendRandomImage(images, user, bot);
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

    private void sendRandomImage(List<ImageEntity> images, UserEntity user, TelegramBot bot) {
        int numberOfImages = images.size();
        ImageEntity image = images.get((int) (Math.random() * numberOfImages));

        log.info(LOG_MARKER, PICKED_IMAGE_LOG_TEMPLATE.formatted(image.getImageUrl(), numberOfImages, user.getChatUserId()));
        sendImage(image, user, bot);
    }

    private String getMessage(UserEntity user) {
        RankEntity newRank = rankRepository.findById(user.getRankLevel()).orElseThrow();
        String message = newRank.getLevelUpMessage();
        return variableReplacer.replaceUserVars(message, user);
    }
}
