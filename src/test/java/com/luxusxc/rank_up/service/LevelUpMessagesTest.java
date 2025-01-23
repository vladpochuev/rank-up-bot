package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.common.model.RankEntity;
import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.common.service.VariableReplacer;
import com.luxusxc.rank_up.web.service.LevelUpMessages;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LevelUpMessagesTest {
    private final LevelUpMessages messages;

    public LevelUpMessagesTest() {
        messages = new LevelUpMessages(new VariableReplacer());
    }

    @Test
    void testImportMessage() {
        RankEntity entity = new RankEntity(1, "TEST", null, null);

        WebConfig webConfig = new WebConfig();
        webConfig.setLevelUpMessage("{newrank}, {newlvl}");
        messages.fillMessage(webConfig, List.of(entity));

        assertThat(entity.getLevelUpMessage(), equalTo("TEST, 1"));
    }

    @Test
    void testImportMessageNull() {
        RankEntity rankEntity = new RankEntity(null, 10L, "{newrank}");
        assertThrows(NullPointerException.class, () -> messages.fillMessage(null, List.of(rankEntity)));
    }

    @Test
    void testImportMessageNullField() {
        RankEntity rankEntity = new RankEntity(null, 10L, "{newrank}");

        WebConfig webConfig = new WebConfig();
        webConfig.setLevelUpMessage(null);
        assertThrows(IllegalArgumentException.class, () -> messages.fillMessage(webConfig, List.of(rankEntity)));
    }

    @Test
    void testImportMessageEmpty() {
        RankEntity rankEntity = new RankEntity(null, 10L, "{newrank}");

        WebConfig webConfig = new WebConfig();
        webConfig.setLevelUpMessage("");
        messages.fillMessage(webConfig, List.of(rankEntity));
        assertThat(rankEntity.getLevelUpMessage(), equalTo(""));
    }
}
