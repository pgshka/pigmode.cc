package me.client.api.manager;

public interface Manager<T> {

    T load();

    T unload();

}