package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.service.StringSplitter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StringSplitterTest {
    private final StringSplitter splitter = new StringSplitter();

    @Test
    void testCommonString() {
        assertThat(splitter.split("Hello", " "), equalTo(List.of("Hello")));
        assertThat(splitter.split("Hello", "123"), equalTo(List.of("Hello")));
        assertThat(splitter.split("Hello World", "123"), equalTo(List.of("Hello World")));
        assertThat(splitter.split("Hello World", ""), equalTo(List.of("Hello World")));
        assertThat(splitter.split("Hello_World", " "), equalTo(List.of("Hello_World")));
        assertThat(splitter.split("Hello World", " "), equalTo(List.of("Hello", "World")));
        assertThat(splitter.split("Hello World!", " "), equalTo(List.of("Hello", "World!")));
        assertThat(splitter.split("Hello\tWorld!", " "), equalTo(List.of("Hello\tWorld!")));
        assertThat(splitter.split("HellotttWorld!", "t"), equalTo(List.of("Hello", "World!")));
    }

    @Test
    void testStringWithSequentialSpaces() {
        assertThat(splitter.split("    Hello World!", " "), equalTo(List.of("Hello", "World!")));
        assertThat(splitter.split("Hello     World!", " "), equalTo(List.of("Hello", "World!")));
        assertThat(splitter.split("Hello World!    ", " "), equalTo(List.of("Hello", "World!")));
    }

    @Test
    void testEmptyString() {
        assertThat(splitter.split("", ""), equalTo(List.of("")));
        assertThat(splitter.split("", "123"), equalTo(List.of()));
    }

    @Test
    void testNull() {
        assertThrows(IllegalArgumentException.class, () -> splitter.split(null, ""));
        assertThrows(IllegalArgumentException.class, () -> splitter.split("Hello World!", null));
    }
}
