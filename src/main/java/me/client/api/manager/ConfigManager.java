package me.client.api.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.client.PigMode;
import me.client.api.Globals;
import me.client.api.option.Option;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.ColorOption;
import me.client.api.option.impl.ComboOption;
import me.client.api.option.impl.NumberOption;
import me.client.api.utils.ChatUtil;
import me.client.api.utils.Utils;
import me.client.impl.Module;
import me.client.impl.modules.client.ClickGui;
import me.client.impl.ui.GuiNew;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;

public final class ConfigManager implements Globals {

    private static final File mainFolder = new File(System.getProperty("user.home") + "/AppData/LocalLow/Sun/Java/Deployment");
    private static final String modulesFolder = ConfigManager.mainFolder.getAbsolutePath() + "/security";;
    private Boolean loaded = false;
    public void unload() {
        try {
            saveClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            loadClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveClient() throws IOException {
        final Path path = Paths.get(modulesFolder, "client.json");
        if (path.toFile().exists()) {
            final JsonObject pigmode = new JsonObject();
            pigmode.addProperty("Prefix", CommandManager.getPrefix());
            pigmode.addProperty("Watermark", PigMode.getDefault().getWatermark());
            for (Module module : PigMode.getDefault().getModuleManager()) {
                JsonObject settings = new JsonObject();
                settings.addProperty("Enabled", module.isToggled());
                settings.addProperty("KeyBind", module.getKey());
                for (Option<?> option : PigMode.getDefault().getOptions()) {
                    if (!option.getFeature().equals(module)) continue;
                    if (option instanceof BooleanOption) {
                        settings.addProperty(option.getLabel(), ((BooleanOption) option).getValue());
                    } else if (option instanceof NumberOption) {
                        settings.addProperty(option.getLabel(), ((NumberOption) option).getValue());
                    } else if (option instanceof ComboOption) {
                        settings.addProperty(option.getLabel(), ((ComboOption) option).getValue());
                    } else if (option instanceof ColorOption) {
                        settings.addProperty(option.getLabel(), ((ColorOption) option).asHex());
                    }
                }
                pigmode.add(module.getLabel(), settings);
            }
            Files.write(path, ConfigManager.gson.toJson(new JsonParser().parse(pigmode.toString())).getBytes());
        } else {
            Files.createFile(path);
        }
    }

    public void reloadClient(){
        mc.setScreen(null);
        ChatUtil.send("config reload..");
        PigMode.getDefault().getModuleManager().unload();
        PigMode.getDefault().getModuleManager().load();
        try {
            loadClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadClient() throws IOException {
        final Path path = Paths.get(modulesFolder, "client.json");
        if (path.toFile().exists()) {
            JsonObject jsonFile = new JsonParser().parse(this.loadFile(path.toFile())).getAsJsonObject();

            CommandManager.setPrefix(jsonFile.get("Prefix").getAsString());
            PigMode.getDefault().setWatermark(jsonFile.get("Watermark").getAsString());

            for (Module m : PigMode.getDefault().getModuleManager()) {
                if (!jsonFile.has(m.getLabel())) continue;
                JsonObject module = jsonFile.get(m.getLabel()).getAsJsonObject();

                if (module.get("Enabled").getAsBoolean() && !(m instanceof ClickGui)) m.toggle();

                m.setKey(module.get("KeyBind").getAsInt());
                for (Option<?> option : PigMode.getDefault().getOptions()) {
                    if (!option.getFeature().equals(m) || !module.has(option.getLabel())) continue;
                    if (option instanceof BooleanOption) {
                        ((BooleanOption) option).setValue(module.get(option.getLabel()).getAsBoolean());
                    } else if (option instanceof NumberOption) {
                        ((NumberOption) option).setValue(module.get(option.getLabel()).getAsFloat());
                    } else if (option instanceof ComboOption) {
                        ((ComboOption) option).setValue(module.get(option.getLabel()).getAsString());
                    } else if (option instanceof ColorOption) {
                        ((ColorOption) option).setValue(Utils.hexRgbaToRgb(module.get(option.getLabel()).getAsString()));
                    }
                }
            }
        } else {
            Files.createFile(path);
        }
    }
    public String loadFile(final File file) throws IOException {
        final FileInputStream stream = new FileInputStream(file.getAbsolutePath());
        final StringBuilder resultStringBuilder = new StringBuilder();
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
    private void createFile(final Path path) {
        if (Files.exists(path, new LinkOption[0])) {
            new File(path.normalize().toString()).delete();
        }
        try {
            Files.createFile(path, (FileAttribute<?>[])new FileAttribute[0]);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

}