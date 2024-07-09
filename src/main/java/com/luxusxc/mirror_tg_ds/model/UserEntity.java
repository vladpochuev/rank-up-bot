package com.luxusxc.mirror_tg_ds.model;

import com.luxusxc.mirror_tg_ds.status.StatusType;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity(name = "users")
@Data
public class UserEntity {
    @Id
    private long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private String languageCode;
    private Timestamp registeredAt;
    private StatusType userStatus;
}