package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.Rank;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.model.UserEntity;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VariableReplacerTest {
    private final VariableReplacer replacer = new VariableReplacer();
    @Test
    void testReplaceRankVars() {
        Rank rank = new Rank("Cherub", 1);
        RankEntity rankEntity = new RankEntity(rank, 30L, null);
        assertThat(replacer.replaceRankVars("{newlvl}: {newrank}", rankEntity), equalTo("1: Cherub"));
        assertThat(replacer.replaceRankVars("newlvl}: newrank}", rankEntity), equalTo("newlvl}: newrank}"));
        assertThat(replacer.replaceRankVars("{newlvl: {newrank", rankEntity), equalTo("{newlvl: {newrank"));
    }

    @Test
    void testReplaceUserVars() {
        UserEntity userEntity = new UserEntity(1, "John", "johndoe", "EN", null, null,  10);
        assertThat(replacer.replaceUserVars("Hello, {name}", userEntity), equalTo("Hello, John"));
        assertThat(replacer.replaceUserVars("Hello, name}", userEntity), equalTo("Hello, name}"));
        assertThat(replacer.replaceUserVars("Hello, {name", userEntity), equalTo("Hello, {name"));
    }
}
