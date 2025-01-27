package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.common.model.Config;
import com.luxusxc.rank_up.common.service.ConfigHandler;
import com.luxusxc.rank_up.common.service.StringSplitter;
import com.luxusxc.rank_up.telegram.config.BotConfig;
import com.luxusxc.rank_up.telegram.service.*;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberLeft;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DecisionCenterTest {
    private final BotJoinChatProcessor botJoinChatProcessor;
    private final BotLeftChatProcessor botLeftChatProcessor;
    private final UserLeftChatProcessor userLeftChatProcessor;
    private final ChatMessageProcessor chatMessageProcessor;
    private final CommandProcessor commandProcessor;
    private final CallbackProcessor callbackProcessor;
    private final DecisionCenter decisionCenter;
    private final ConfigHandler configHandler;

    public DecisionCenterTest() {
        this.botJoinChatProcessor = mock();
        this.botLeftChatProcessor = mock();
        this.userLeftChatProcessor = mock();
        this.chatMessageProcessor = mock();
        this.commandProcessor = mock();
        this.callbackProcessor = mock();
        this.configHandler = mock();
        StringSplitter splitter = new StringSplitter();
        BotConfig config = new BotConfig("CustomRankUpBot", "123");
        this.decisionCenter = new DecisionCenter(botJoinChatProcessor, botLeftChatProcessor, userLeftChatProcessor, chatMessageProcessor, commandProcessor, callbackProcessor, new ChatMemberStatus(), configHandler, new CommandParser(splitter, config));
    }

    @Test
    void testProcessUpdateJoinChat() {
        Update update = new Update();
        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setOldChatMember(new ChatMemberMember());
        chatMemberUpdated.setNewChatMember(new ChatMemberAdministrator());
        update.setMyChatMember(chatMemberUpdated);

        decisionCenter.processUpdate(update);
        verify(botJoinChatProcessor).updateChatInfo(any());
    }

    @Test
    void testProcessUpdateBotLeft() {
        Update update = new Update();
        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setOldChatMember(new ChatMemberAdministrator());
        chatMemberUpdated.setNewChatMember(new ChatMemberLeft());
        update.setMyChatMember(chatMemberUpdated);

        decisionCenter.processUpdate(update);
        verify(botLeftChatProcessor).processLeave(any());
    }

    @Test
    void testProcessUpdateUserLeft() {
        Update update = new Update();
        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setOldChatMember(new ChatMemberMember());
        chatMemberUpdated.setNewChatMember(new ChatMemberLeft());
        update.setChatMember(chatMemberUpdated);

        decisionCenter.processUpdate(update);
        verify(userLeftChatProcessor).processLeave(any());
    }

    @Test
    void testProcessUpdateGroupMessage() {
        Update update = new Update();
        Message message = new Message();
        message.setChat(new Chat(-1L, "supergroup"));
        message.setText("test");
        update.setMessage(message);

        Config config = new Config();
        config.setEnableAll(true);
        when(configHandler.getConfig()).thenReturn(config);

        decisionCenter.processUpdate(update);
        verify(chatMessageProcessor).processMessage(any());
    }

    @Test
    void testProcessUpdateGroupMessageDisabled() {
        Update update = new Update();
        Message message = new Message();
        message.setChat(new Chat(-1L, "supergroup"));
        message.setText("test");
        update.setMessage(message);

        Config config = new Config();
        config.setEnableAll(false);
        when(configHandler.getConfig()).thenReturn(config);

        decisionCenter.processUpdate(update);
        verify(chatMessageProcessor, times(0)).processMessage(any());
    }

    @Test
    void testProcessUpdateGroupCommand() {
        Update update = new Update();
        Message message = new Message();
        message.setChat(new Chat(-1L, "group"));
        message.setText("/stats");
        update.setMessage(message);

        decisionCenter.processUpdate(update);
        verify(commandProcessor).processGroupCommand(any());
    }

    @Test
    void testProcessUpdateGroupDirectCommand() {
        Update update = new Update();
        Message message = new Message();
        message.setChat(new Chat(-1L, "group"));
        message.setText("/stats@CustomRankUpBot");
        update.setMessage(message);

        decisionCenter.processUpdate(update);
        verify(commandProcessor).processGroupCommand(any());
    }

    @Test
    void testProcessUpdateDirectMessage() {
        Update update = new Update();
        Message message = new Message();
        message.setChat(new Chat(-1L, "private"));
        message.setText("test");
        update.setMessage(message);

        decisionCenter.processUpdate(update);
        verify(commandProcessor).processUserCommand(any());
    }

    @Test
    void testProcessUpdateCallback() {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());

        decisionCenter.processUpdate(update);
        verify(callbackProcessor).processCallback(any());
    }
}
