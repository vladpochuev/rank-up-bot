package com.luxusxc.rank_up.telegram.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private ChatUserId chatUserId;
    private String firstName;
    private String lastName;
    private String userName;
    private String languageCode;
    private Timestamp registeredAt;
    private Integer rankLevel;
    private Long experience;

    public UserEntity(ChatUserId chatUserId) {
        this.chatUserId = chatUserId;
    }
}
