package com.luxusxc.rank_up.model;

@FunctionalInterface
public interface Executable<T> {
    void execute(T t);
}