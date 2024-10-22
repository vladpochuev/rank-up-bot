package com.luxusxc.rank_up.telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Service
public class ChatMemberStatus {
    public boolean isMember(ChatMember chatMember) {
        String status = chatMember.getStatus();
        return status.equals("member");
    }

    public boolean isLeft(ChatMember chatMember) {
        String status = chatMember.getStatus();
        return status.equals("left");
    }

    public boolean isKicked(ChatMember chatMember) {
        String status = chatMember.getStatus();
        return status.equals("kicked");
    }

    public boolean isAdmin(ChatMember chatMember) {
        String status = chatMember.getStatus();
        return status.equals("administrator");
    }
}
