package com.luxusxc.rank_up.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "default_ranks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultRankEntity {
    @Id
    private Integer level;
    private String name;
    private Long experience;

    public DefaultRankEntity(String name, Long experience) {
        this.name = name;
        this.experience = experience;
    }
}
