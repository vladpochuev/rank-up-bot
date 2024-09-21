package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.service.StringJoiner;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StringJoinerTest {
    private final StringJoiner joiner = new StringJoiner();

    @Test
    void testCommonString() {
        assertThat(joiner.join(List.of("Hello"), " "), equalTo("Hello"));
        assertThat(joiner.join(List.of("Hello", "World"), " "), equalTo("Hello World"));
        assertThat(joiner.join(List.of("Hello", "World", "Hello"), " "), equalTo("Hello World Hello"));
    }

    @Test
    void testEmpty() {
        assertThat(joiner.join(List.of(""), " "), equalTo(""));
        assertThat(joiner.join(List.of("", "Hello", ""), " "), equalTo(" Hello "));
        assertThat(joiner.join(List.of("Hello", "", "World"), " "), equalTo("Hello  World"));
        assertThat(joiner.join(List.of("Hello"), ""), equalTo("Hello"));
    }

    @Test
    void testNull() {
        assertThrows(IllegalArgumentException.class, () -> joiner.join(null, ""));
        assertThrows(IllegalArgumentException.class, () -> joiner.join(List.of("Hello", "World"), null));
        assertThrows(IllegalArgumentException.class, () -> joiner.join(null, null));
    }

    @Test
    void testContainsNull() {
        List<String> list = new ArrayList<>();
        list.add(null);
        assertThrows(IllegalArgumentException.class, () -> joiner.join(list, ""));
    }

    @Test
    void testContainsDataAndNull() {
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        list.add(null);
        assertThrows(IllegalArgumentException.class, () -> joiner.join(list, ""));
    }
}
