package com.luxusxc.rank_up.telegram.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatEntity {
    @Id
    private long id;
    private String title;
    private Integer date;
}
