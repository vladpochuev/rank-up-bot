package com.luxusxc.rank_up.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StatsMessage {
    private ChatEntity chat;
    private RankEntity highestRank;
    private UserEntity highestRankUser;
    private int activeMembers;
}
