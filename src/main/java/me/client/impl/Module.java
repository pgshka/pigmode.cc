package me.client.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.client.PigMode;
import me.client.api.Pair;
import me.client.api.option.Option;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;

public class Module extends Feature.ToggleableFeature {

    private final Category category;
    private boolean toggled;
    private int key = GLFW_KEY_UNKNOWN;


    public Module(String name, Category category) {
        this(name, "No Description provided!", category);
    }

    public Module(String name, String desc, Category category) {
        super(name, desc);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public static boolean nullCheck(){
        return mc.player == null || mc.world == null;
    }

    public void tick() { }

    @Override public boolean isToggled() {
        return toggled;
    }

    @Override public void enable() {
        toggled = true;
        onToggle();
        onEnable();
        PigMode.EVENT_BUS.register(this);
    }

    @Override public void disable() {
        onToggle();
        onDisable();
        toggled = false;
        PigMode.EVENT_BUS.unregister(this);
    }

    public void onEnable() { }

    public void onDisable() { }

    public void onToggle() { }


    public enum Category {
        GLOBAL("Global"),
        ANARCHY("Anarchy"),
        WYNNCRAFT("WynnCraft"),
        LEGIT("Legit"),
        CLIENT("Client");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}