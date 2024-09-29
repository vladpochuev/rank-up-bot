package com.luxusxc.rank_up.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer level;
    private String name;
    private Long experience;
    private String levelUpMessage;

    public RankEntity(String name, Long experience, String levelUpMessage) {
        this.name = name;
        this.experience = experience;
        this.levelUpMessage = levelUpMessage;
    }
}