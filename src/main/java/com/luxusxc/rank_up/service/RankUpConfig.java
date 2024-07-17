package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.repository.ImageRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RankUpConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean enableAll;
    private boolean enableCustomRanks;
    private boolean enableCustomLevels;
    private boolean announceLevelUp;
    private String levelUpMessageFormat;
    private transient RankRepository rankRepository;
    private transient ImageRepository imageRepository;

    public RankUpConfig(RankRepository rankRepository, ImageRepository imageRepository) {
        this.rankRepository = rankRepository;
        this.imageRepository = imageRepository;
    }
}
