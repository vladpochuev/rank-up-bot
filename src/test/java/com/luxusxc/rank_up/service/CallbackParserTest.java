package com.luxusxc.rank_up.service;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CallbackParserTest {
    private final CallbackParser parser = new CallbackParser(new StringSplitter());

    @Test
    void testCommonCallback() {
        assertThat(parser.getPrefix("CALLBACK_TEST"), equalTo("CALLBACK"));
        assertThat(parser.getPrefix("CALLBACK"), equalTo("CALLBACK"));
    }

    @Test
    void testSeveralSlashCallback() {
        assertThat(parser.getPrefix("CALLBACK_TEST_CHECK"), equalTo("CALLBACK"));
    }

    @Test
    void testTrimCallback() {
        assertThat(parser.getPrefix("CALLBACK_TEST "), equalTo("CALLBACK"));
        assertThat(parser.getPrefix(" CALLBACK_TEST"), equalTo("CALLBACK"));
    }

    @Test
    void testEmptyOrNullCallback() {
        assertThrows(IllegalArgumentException.class, () -> parser.getPrefix(""));
        assertThrows(IllegalArgumentException.class, () -> parser.getPrefix(null));
    }
}
