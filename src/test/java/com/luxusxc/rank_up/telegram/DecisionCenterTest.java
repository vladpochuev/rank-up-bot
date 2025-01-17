package com.luxusxc.rank_up.telegram;

import com.luxusxc.rank_up.config.BotConfig;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberLeft;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DecisionCenterTest {
    private final BotJoinChatProcessor botJoinChatProcessor;
    private final BotLeftChatProcessor botLeftChatProcessor;
    private final UserLeftChatProcessor userLeftChatProcessor;
    private final ChatMessageProcessor chatMessageProcessor;
    private final CommandProcessor commandProcessor;
    private final CallbackProcessor callbackProcessor;
    private final DecisionCenter decisionCenter;

    public DecisionCenterTest() {
        this.botJoinChatProcessor = mock();
        this.botLeftChatProcessor = mock();
        this.userLeftChatProcessor = mock();
        this.chatMessageProcessor = mock();
        this.commandProcessor = mock();
        this.callbackProcessor = mock();
        this.decisionCenter = new DecisionCenter(botJoinChatProcessor, botLeftChatProcessor, userLeftChatProcessor, chatMessageProcessor, commandProcessor, callbackProcessor, new ChatMemberStatus(), new BotConfig("CustomRankUpBot", "123"));
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

        decisionCenter.processUpdate(update);
        verify(chatMessageProcessor).processMessage(any());
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
