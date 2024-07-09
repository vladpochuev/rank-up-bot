package com.luxusxc.mirror_tg_ds.commands;

import com.luxusxc.mirror_tg_ds.model.UserEntity;
import com.luxusxc.mirror_tg_ds.service.TelegramBot;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.Timestamp;

@Slf4j
public class StartCommand extends Command {
    private static final String START_MESSAGE_FORMAT = """
            Hi, %s, nice to meet you!""";
    private static final String USER_EXISTS_MESSAGE = "You are already logged in";

    public StartCommand(TelegramBot bot) {
        super(bot);
    }

    public void execute(Message message) {
        long chatId = message.getChatId();
        String name = message.getChat().getFirstName();

        if (bot.getUserRepository().findById(chatId).isEmpty()) {
            registerUser(message);
            welcomeUser(chatId, name);
        } else {
            bot.sendMessage(chatId, USER_EXISTS_MESSAGE);
        }
    }

    private void registerUser(Message message) {
        User user = message.getFrom();

        UserEntity entity = new UserEntity();
        entity.setChatId(message.getChatId());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setUserName(user.getUserName());
        entity.setLanguageCode(user.getLanguageCode());
        entity.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

        bot.getUserRepository().save(entity);
        log.info("User saved: " + entity);
    }

    private void welcomeUser(long chatId, String name) {
        SendMessage message = new SendMessage();
        String text = EmojiParser.parseToUnicode(START_MESSAGE_FORMAT.formatted(name));
        message.setText(text);
        message.setChatId(chatId);
        log.info("Replied to user " + name);
        bot.sendMessage(message);
    }
}
