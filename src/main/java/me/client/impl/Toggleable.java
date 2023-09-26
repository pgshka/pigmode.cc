package me.client.impl;

public interface Toggleable {

    boolean isToggled();

    void enable();

    void disable();

    default void setToggled(boolean state) {
        if (state) enable();
        else disable();
    }

    default void toggle() {
        setToggled(!isToggled());
    }
}