package com.luxusxc.rank_up.telegram;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DecisionCenterTest {
    private final JoinChatProcessor joinChatProcessor;
    private final ChatMessageProcessor chatMessageProcessor;
    private final CommandProcessor commandProcessor;
    private final CallbackProcessor callbackProcessor;
    private final DecisionCenter decisionCenter;

    public DecisionCenterTest() {
        this.joinChatProcessor = mock();
        this.chatMessageProcessor = mock();
        this.commandProcessor = mock();
        this.callbackProcessor = mock();
        this.decisionCenter = new DecisionCenter(joinChatProcessor, chatMessageProcessor, commandProcessor, callbackProcessor);
    }

    @Test
    void testProcessUpdateJoinChat() {
        Update update = new Update();
        update.setMyChatMember(new ChatMemberUpdated());

        decisionCenter.processUpdate(update);
        verify(joinChatProcessor).updateChatInfo(any());
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
    void testProcessUpdateDirectMessage() {
        Update update = new Update();
        Message message = new Message();
        message.setChat(new Chat(-1L, "private"));
        message.setText("test");
        update.setMessage(message);

        decisionCenter.processUpdate(update);
        verify(commandProcessor).processCommand(any());
    }

    @Test
    void testProcessUpdateCallback() {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());

        decisionCenter.processUpdate(update);
        verify(callbackProcessor).processCallback(any());
    }
}
