package com.luxusxc.rank_up.model;

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
    private String rank;
    private Long experience;
}
