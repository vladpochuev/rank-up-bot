package com.luxusxc.rank_up.telegram.commands;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class CommandTypeTest {
    @Test
    void testGetInstance() {
        String command = "/start";
        assertThat(CommandType.getInstance(command), equalTo(CommandType.START));
    }

    @Test
    void testGetInstanceInvalid() {
        String command = "123";
        assertThat(CommandType.getInstance(command), nullValue());
    }

    @Test
    void testGetInstanceNull() {
        String command = null;
        assertThat(CommandType.getInstance(command), nullValue());
    }
}
