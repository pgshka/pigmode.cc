package me.client.api;

import org.jetbrains.annotations.NotNull;

public class EventBus extends com.google.common.eventbus.EventBus {

    @Override public void post(@NotNull Object event) {
        super.post(event);
    }
}