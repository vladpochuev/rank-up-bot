package com.luxusxc.rank_up.telegram.model;

@FunctionalInterface
public interface Executable<T> {
    void execute(T t);
}