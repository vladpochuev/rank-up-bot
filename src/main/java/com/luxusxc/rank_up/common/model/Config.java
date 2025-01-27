package com.luxusxc.rank_up.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean enableAll;
    private boolean enableCustomRanks;
    private boolean enableCustomLevels;
    private boolean announceLevelUp;
    private String levelUpMessageFormat;
}
