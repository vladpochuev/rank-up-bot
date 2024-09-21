package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.Rank;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.RankRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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
        messages = new LevelUpMessages(repository, new VariableReplacer());
    }

    @Test
    void testImportMessage() {
        RankEntity entity = new RankEntity(new Rank("TEST", 1), null, null);
        when(repository.findAll()).thenReturn(List.of(entity));

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage("{newrank}, {newlvl}");
        messages.importMessage(webConfig);

        ArgumentCaptor<List<RankEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());

        RankEntity capturedRankEntity = captor.getValue().get(0);
        assertThat(capturedRankEntity.getLevelUpMessage(), equalTo("TEST, 1"));
    }

    @Test
    void testImportMessageNull() {
        assertThrows(NullPointerException.class, () -> messages.importMessage(null));
    }

    @Test
    void testImportMessageNullField() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage(null);
        assertThrows(IllegalArgumentException.class, () -> messages.importMessage(webConfig));
    }

    @Test
    void testImportLevelsEmpty() {
        RankEntity rankEntity = new RankEntity(null, 10L, "{newrank}");
        when(repository.findAll()).thenReturn(List.of(rankEntity));

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage("");
        assertThrows(IllegalArgumentException.class, () -> messages.importMessage(webConfig));
    }
}
