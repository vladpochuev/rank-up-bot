package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.Rank;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.RankRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LevelUpMessagesTest {
    private final RankRepository repository;
    private final LevelUpMessages messages;

    public LevelUpMessagesTest() {
        repository = mock();
        messages = new LevelUpMessages(new VariableReplacer());
    }

    @Test
    void testImportMessage() {
        RankEntity entity = new RankEntity(new Rank("TEST", 1), null, null);

        WebRankUpConfig webConfig = new WebRankUpConfig();
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

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage(null);
        assertThrows(IllegalArgumentException.class, () -> messages.fillMessage(webConfig, List.of(rankEntity)));
    }

    @Test
    void testImportLevelsEmpty() {
        RankEntity rankEntity = new RankEntity(null, 10L, "{newrank}");

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage("");
        assertThrows(IllegalArgumentException.class, () -> messages.fillMessage(webConfig, List.of(rankEntity)));
    }
}
