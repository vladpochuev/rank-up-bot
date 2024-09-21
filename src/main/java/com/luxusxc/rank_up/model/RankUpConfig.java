package com.luxusxc.rank_up.model;

import com.luxusxc.rank_up.repository.ImageRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankUpConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean enableAll;
    private boolean enableCustomRanks;
    private boolean enableCustomLevels;
    private boolean announceLevelUp;
    private String levelUpMessageFormat;
}
