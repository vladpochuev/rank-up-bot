package com.luxusxc.rank_up.service;

@FunctionalInterface
public interface Executable<T> {
    void execute(T t);
}