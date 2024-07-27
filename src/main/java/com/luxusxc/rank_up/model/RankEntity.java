package com.luxusxc.rank_up.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "ranks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankEntity {
    @Id
    private String rank;
    private Integer level;
    private Long experience;
    private String levelUpMessage;

    public RankEntity(String rank) {
        this.rank = rank;
    }
}