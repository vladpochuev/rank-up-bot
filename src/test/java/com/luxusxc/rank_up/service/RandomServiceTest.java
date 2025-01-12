package com.luxusxc.rank_up.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RandomServiceTest {
    private RandomService randomService;
    private Random random;

    @BeforeEach
    void init() {
        random = mock();
        randomService = new RandomService();
        randomService.setRandom(random);
    }

    @Test
    void testGetRandomElementSingleElementList() {
        List<String> list = List.of("Single");
        when(random.nextInt(list.size())).thenReturn(0);
        String result = randomService.getRandomElement(list);
        assertThat(result, equalTo("Single"));
    }

    @Test
    void testGetRandomElementEmptyList() {
        List<String> list = List.of();
        assertThrows(IllegalArgumentException.class, () -> randomService.getRandomElement(list));
    }

    @Test
    void testGetRandomElementMultipleElementsFirstIndex() {
        List<Integer> list = List.of(10, 20, 30);
        when(random.nextInt(list.size())).thenReturn(0);
        Integer result = randomService.getRandomElement(list);
        assertThat(result, equalTo(10));
    }

    @Test
    void testGetRandomElementMultipleElementsLastIndex() {
        List<Integer> list = List.of(10, 20, 30);
        when(random.nextInt(list.size())).thenReturn(2);
        Integer result = randomService.getRandomElement(list);
        assertThat(result, equalTo(30));
    }

    @Test
    void testGetRandomElementStringList() {
        List<String> list = List.of("Apple", "Banana", "Cherry", "Date");
        when(random.nextInt(list.size())).thenReturn(3);
        String result = randomService.getRandomElement(list);
        assertThat(result, equalTo("Date"));
    }
}
