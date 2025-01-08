package com.luxusxc.rank_up.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CallbackParserTest {
    private final CallbackParser parser = new CallbackParser(new StringSplitter());

    @Test
    void testGetPrefixCommon() {
        assertThat(parser.getPrefix("CALLBACK_TEST"), equalTo("CALLBACK"));
        assertThat(parser.getPrefix("CALLBACK"), equalTo("CALLBACK"));
    }

    @Test
    void testGetPrefixSeveralArgs() {
        assertThat(parser.getPrefix("CALLBACK_TEST_CHECK"), equalTo("CALLBACK"));
    }

    @Test
    void testGetPrefixTrim() {
        assertThat(parser.getPrefix("CALLBACK_TEST "), equalTo("CALLBACK"));
        assertThat(parser.getPrefix(" CALLBACK_TEST"), equalTo("CALLBACK"));
    }

    @Test
    void testGetPrefixBlank() {
        assertThrows(IllegalArgumentException.class, () -> parser.getPrefix(""));
        assertThrows(IllegalArgumentException.class, () -> parser.getPrefix(null));
    }

    @Test
    void testGetArgsCommon() {
        assertThat(parser.getArgs("CALLBACK_ARG"), equalTo(List.of("ARG")));
    }

    @Test
    void testGetArgsSeveralArgs() {
        assertThat(parser.getArgs("CALLBACK_FIRST_SECOND"), equalTo(List.of("FIRST", "SECOND")));
    }

    @Test
    void testGetArgsOnlyPrefix() {
        assertThat(parser.getArgs("CALLBACK"), equalTo(List.of()));
    }

    @Test
    void testGetArgsTrim() {
        assertThat(parser.getArgs("CALLBACK_TEST "), equalTo(List.of("TEST")));
        assertThat(parser.getArgs(" CALLBACK_TEST"), equalTo(List.of("TEST")));
        assertThat(parser.getArgs(" CALLBACK_TEST   "), equalTo(List.of("TEST")));
    }

    @Test
    void testGetArgsBlank() {
        assertThrows(IllegalArgumentException.class, () -> parser.getArgs(""));
        assertThrows(IllegalArgumentException.class, () -> parser.getArgs(null));
    }
}
