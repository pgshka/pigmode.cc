package me.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import me.client.api.Globals;
import me.client.api.EventBus;
import me.client.api.manager.CommandManager;
import me.client.api.manager.ConfigManager;
import me.client.api.manager.ModuleManager;
import me.client.api.option.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PigMode implements Globals {

    public static MinecraftClient mc;
    private static PigMode INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger("Ferret");
    public static String WATERMARK = "pigmode.cc";
    private final ModuleManager moduleManager = new ModuleManager();
    private ConfigManager configManager = new ConfigManager();
    private final CommandManager commands = new CommandManager();

    public PigMode() {
    }

    public String getWatermark() {
        return WATERMARK;
    }
    public String setWatermark(String value) {
        return WATERMARK = value;
    }

    public static final EventBus EVENT_BUS = new EventBus();

    public void init() {
        System.out.println("init");
        commands.load();
        moduleManager.load();
        configManager.load();
        EVENT_BUS.register(new EventHandler());
    }

    public static PigMode getDefault() {
        if (INSTANCE == null) INSTANCE = new PigMode();
        return INSTANCE;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commands;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public List<Option<?>> getOptions() {
        return Option.getOptions();
    }
}