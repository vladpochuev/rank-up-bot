package com.luxusxc.rank_up.service;

import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Setter
public class RandomService {
    private Random random = ThreadLocalRandom.current();

    public <T> T getRandomElement(List<T> list) {
        throwIfEmpty(list);
        int listSize = list.size();
        return list.get(random.nextInt(listSize));
    }

    private <T> void throwIfEmpty(List<T> list) {
        if (list.equals(List.of())) {
            throw new IllegalArgumentException("Provided message is empty");
        }
    }
}
