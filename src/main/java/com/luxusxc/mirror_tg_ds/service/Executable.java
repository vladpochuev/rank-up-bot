package com.luxusxc.mirror_tg_ds.service;

@FunctionalInterface
public interface Executable<T> {
    void execute(T t);
}