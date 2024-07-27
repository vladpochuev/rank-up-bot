package com.luxusxc.rank_up.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StringJoiner {
    public String join(List<String> strings, String separator) {
        return String.join(separator, strings);
    }
}
