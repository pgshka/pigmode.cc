package me;

import me.client.PigMode;
import net.fabricmc.api.ModInitializer;

public class FabricEntryPoint implements ModInitializer {

    @Override public void onInitialize() {
        System.out.println("init");
        PigMode.getDefault().init();
    }
}