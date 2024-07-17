package com.luxusxc.rank_up.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {
    @Id
    private String imageUrl;
}
