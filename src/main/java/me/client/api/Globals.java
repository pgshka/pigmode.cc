package me.client.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.client.PigMode;
import net.minecraft.client.MinecraftClient;

public interface Globals {

    MinecraftClient mc = MinecraftClient.getInstance();
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    default PigMode pigmode() {
        return PigMode.getDefault();
    }
    static MinecraftClient mc() {
        return MinecraftClient.getInstance();
    }
}