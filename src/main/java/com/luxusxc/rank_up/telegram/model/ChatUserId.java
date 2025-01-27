package com.luxusxc.rank_up.telegram.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ChatUserId implements Serializable {
    private long chatId;
    private long userId;

    @Override
    public String toString() {
        return "chatId=" + chatId + ", userId=" + userId;
    }
}
