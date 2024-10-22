package com.luxusxc.rank_up.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CommandParserTest {
    private final CommandParser parser = new CommandParser(new StringSplitter());
    @Test
    void testCommonMessageBody() {
        assertThat(parser.getCommandBody("/send hello"), equalTo("/send"));
        assertThat(parser.getCommandBody("/send"), equalTo("/send"));
    }

    @Test
    void testSequentialSpacesBody() {
        assertThat(parser.getCommandBody("/send   "), equalTo("/send"));
        assertThat(parser.getCommandBody("    /send"), equalTo("/send"));
    }

    @Test
    void testEmptyOrNullBody() {
        assertThrows(IllegalArgumentException.class, () -> parser.getCommandBody(""));
        assertThrows(IllegalArgumentException.class, () -> parser.getCommandBody(null));
    }

    @Test
    void testCommonMessageArgs() {
        assertThat(parser.getArgs("/send hello").get(0), equalTo("hello"));
        assertThat(parser.getArgs("/send hello world").get(0), equalTo("hello"));
        assertThat(parser.getArgs("/send hello world").get(1), equalTo("world"));
        assertThat(parser.getArgs("/send hello world"), equalTo(List.of("hello", "world")));
        assertThat(parser.getArgs("/send 123").get(0), equalTo("123"));
        assertThat(parser.getArgs("/send"), equalTo(List.of()));
    }

    @Test
    void testSequentialSpacesArgs() {
        assertThat(parser.getArgs("   /send hello world").get(0), equalTo("hello"));
        assertThat(parser.getArgs("/send  hello world").get(1), equalTo("world"));
    }

    @Test
    void testEmptyOrNullArgs() {
        assertThrows(IllegalArgumentException.class, () -> parser.getArgs(""));
        assertThrows(IllegalArgumentException.class, () -> parser.getArgs(null));
    }
}
