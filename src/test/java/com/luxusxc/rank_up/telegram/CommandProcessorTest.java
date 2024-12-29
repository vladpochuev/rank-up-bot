package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.config.BotConfig;
import com.luxusxc.rank_up.model.BotAction;
import com.luxusxc.rank_up.service.CommandParser;
import com.luxusxc.rank_up.service.StringSplitter;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.mockito.Mockito.*;

@Service
public class CommandProcessorTest {
    private final CommandProcessor commandProcessor;
    private final TelegramBot bot;

    public CommandProcessorTest() {
        this.commandProcessor = new CommandProcessor(new CommandParser(new StringSplitter(), new BotConfig("CustomRankUpBot", "123")));
        this.bot = mock();
    }

    @Test
    void testProcessCommandUnknown() {
        Message message = new Message();
        message.setChat(new Chat(-1L, "private"));
        message.setText("test");

        BotAction botAction = commandProcessor.processUserCommand(message);
        botAction.execute(bot);
        verify(bot, times(1)).sendMessage(-1L, "Sorry, command was not recognized.");
    }

    @Test
    void testProcessCommandDirect() {
        Message message = new Message();
        message.setChat(new Chat(-1L, "group"));
        message.setText("/stat@CustomRankUpBot");

        BotAction botAction = commandProcessor.processUserCommand(message);
        botAction.execute(bot);
        verify(bot, times(1)).sendMessage(eq(-1L), any());
    }
}
