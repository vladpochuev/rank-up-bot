package com.luxusxc.rank_up.telegram.commands;

import com.luxusxc.rank_up.telegram.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UnknownCommandTest {
    private TelegramBot bot;
    private Command unknownCommand;

    @BeforeEach
    void init() {
        bot = mock();
        unknownCommand = new UnknownCommand(bot);
    }

    @Test
    void testExecute() {
        Message message = new Message();
        message.setChat(new Chat(123L, ""));
        unknownCommand.execute(message);
        verify(bot).sendMessage(message.getChatId(), "Sorry, command was not recognized.");
    }
}
