package com.luxusxc.rank_up.telegram.command;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class CommandFactoryTest {
    private final CommandFactory commandFactory = new CommandFactory(null);

    @Test
    void testGetCommandCommon() {
        Object command = commandFactory.getCommand(CommandType.START);
        assertThat(command, instanceOf(StartCommand.class));
    }

    @Test
    void testGetCommandNull() {
        Object command = commandFactory.getCommand(null);
        assertThat(command, instanceOf(UnknownCommand.class));
    }
}
