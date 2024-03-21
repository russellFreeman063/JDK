package ru.jdk.repository;

public interface Repository<T> {
    void save(T text);
    T load();
}
