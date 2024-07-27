package com.luxusxc.rank_up.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StringSplitter {
    public List<String> split(String string, String separator) {
        return List.of(string.split(separator));
    }
}
