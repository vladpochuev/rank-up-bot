package com.luxusxc.rank_up.common.service;

import com.luxusxc.rank_up.common.model.RankEntity;
import com.luxusxc.rank_up.telegram.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariableReplacer {
    private final List<Variable> rankVars = List.of(Variable.NEW_RANK, Variable.NEW_LEVEL);
    private final List<Variable> userVars = List.of(Variable.NAME);

    public String replaceRankVars(String message, RankEntity rank) {
        for (Variable rankVar : rankVars) {
            message = replace(message, rank, rankVar);
        }
        return message;
    }

    public String replaceUserVars(String message, UserEntity user) {
        for (Variable userVar : userVars) {
            message = replace(message, user, userVar);
        }
        return message;
    }

    private String replace(String message, Object object, Variable var) {
        String tag = var.getTag();
        String param = var.getCallback().apply(object);
        return message.replaceAll(tag, param);
    }
}
