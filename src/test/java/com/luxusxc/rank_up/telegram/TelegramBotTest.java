package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.config.BotConfig;
import com.luxusxc.rank_up.service.CommandParser;
import com.luxusxc.rank_up.telegram.commands.Command;
import com.luxusxc.rank_up.telegram.commands.CommandType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotTest {
    @InjectMocks
    @Spy
    private TelegramBot bot;
    @Mock
    private BotConfig config;
    @Mock
    private CommandParser commandParser;

    @Test
    void testSetBotCommands() throws TelegramApiException {
        doReturn(null).when(bot).execute(any(SetMyCommands.class));
        bot.setBotCommands();

        ArgumentCaptor<SetMyCommands> captor = ArgumentCaptor.forClass(SetMyCommands.class);
        verify(bot).execute(captor.capture());
        SetMyCommands commands = captor.getValue();

        List<String> expectedCommands = commands.getCommands().stream().map(BotCommand::getCommand).toList();
        List<String> actualCommands = Arrays.stream(CommandType.values()).map(c -> c.body).toList();

        for (String actualCommand : actualCommands) {
            assertTrue(expectedCommands.contains(actualCommand));
        }
        for (String expectedCommand : expectedCommands) {
            assertTrue(actualCommands.contains(expectedCommand));
        }
    }

    @Test
    void testSendMessage() throws TelegramApiException {
        doReturn(null).when(bot).execute(any(SendMessage.class));

        SendMessage message = new SendMessage();
        bot.sendMessage(message);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(captor.capture());

        SendMessage actual = captor.getValue();
        assertThat(actual, equalTo(message));
    }

    @Test
    void testSendMessageChatIdText() throws TelegramApiException {
        doReturn(null).when(bot).execute(any(SendMessage.class));

        long chatId = -1;
        String text = "TEST";
        bot.sendMessage(chatId, text);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(captor.capture());

        SendMessage actual = captor.getValue();
        assertThat(actual.getChatId(), equalTo(String.valueOf(chatId)));
        assertThat(actual.getText(), equalTo(text));
    }

    @Test
    void testSendMessageParseMode() throws TelegramApiException {
        doReturn(null).when(bot).execute(any(SendMessage.class));

        long chatId = -1;
        String text = "TEST";
        String parseMode = "HTML";
        bot.sendMessage(chatId, text, parseMode);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(captor.capture());

        SendMessage actual = captor.getValue();
        SendMessage expected = new SendMessage(String.valueOf(chatId), text);
        expected.setParseMode(parseMode);

        assertThat(actual.getChatId(), equalTo(String.valueOf(chatId)));
        assertThat(actual.getText(), equalTo(text));
        assertThat(actual.getParseMode(), equalTo(parseMode));
    }
}
