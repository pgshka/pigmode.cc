package me.client.impl;

import com.google.gson.JsonObject;
import me.client.api.Globals;

import java.util.Objects;

public class Feature implements Nameable, Globals {

    private final String name;
    private final String desc;

    public Feature(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    @Override public String getLabel() {
        return name;
    }

    public String getDescription() {
        return desc;
    }

    @Override public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = ( Feature ) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.desc, that.desc);
    }

    public static class ToggleableFeature extends Feature implements Toggleable {

        private boolean toggled = true;

        public ToggleableFeature(String name, String desc) {
            super(name, desc);
        }

        @Override public boolean isToggled() {
            return toggled;
        }

        @Override public void enable() {
            toggled = true;
        }

        @Override public void disable() {
            toggled = false;
        }

    }

}