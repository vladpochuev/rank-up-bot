package com.luxusxc.rank_up.telegram.status;

import com.luxusxc.rank_up.telegram.commands.Command;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatusFactoryTest {
    private final StatusFactory statusFactory = new StatusFactory(null);

    @Test
    void testGetStatus() {
        Command command = statusFactory.getStatus(StatusType.DEFAULT);
        assertThat(command, equalTo(null));
    }

    @Test
    void testGetStatusNull() {
        assertThrows(IllegalArgumentException.class, () -> statusFactory.getStatus(null));
    }
}
