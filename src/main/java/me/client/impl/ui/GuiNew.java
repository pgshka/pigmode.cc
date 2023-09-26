package me.client.impl.ui;

import com.mojang.authlib.GameProfile;
import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiTabBarFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.*;
import me.client.PigMode;
import me.client.api.Globals;
import me.client.api.manager.CommandManager;
import me.client.api.option.Option;
import me.client.api.option.impl.*;
import me.client.api.utils.KeyUtil;
import me.client.impl.Module;
import me.client.impl.modules.client.ClickGui;
import me.client.impl.modules.global.FakePlayer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.stream.Collectors;

public class GuiNew extends Screen implements Globals {

    private boolean binding = false;
    private String category = null;
    private final Map<Module, ImBoolean> enabled = new HashMap<>();
    private final Map<Option<?>, Object> settings = new HashMap<>();

    private long windowPtr;
    private final ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 implGl3 = new ImGuiImplGl3();
    private Module module = null;

    public GuiNew() {
        super(Text.of("0"));
        windowPtr = mc.getWindow().getHandle();
        ImGui.createContext();
        implGlfw.init(windowPtr, false);
        implGl3.init("#version 150");

        for (Module m : PigMode.getDefault().getModuleManager()) {
            enabled.put(m, new ImBoolean(m.isToggled()));
        }
        binding = false;
        Module saved = PigMode.getDefault().getModuleManager().get(ClickGui.class).savedModule;
        if (saved != null)
            module = saved;
    }

    OtherClientPlayerEntity fakePlayer;

    @Override
    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        implGlfw.newFrame();
        ImGui.getIO().setIniFilename(null);
        ImGui.newFrame();
        ImGui.getIO().setConfigWindowsMoveFromTitleBarOnly(true);
        setupStyle();

        if (PigMode.getDefault().getModuleManager().get(ClickGui.class).demoGui.getValue()) {
            ImGui.showDemoWindow();
        }
        //-------------------------- MAIN GUI

        ImGui.setNextWindowSize(650, 450);
        if (ImGui.begin(PigMode.WATERMARK, ImGuiWindowFlags.HorizontalScrollbar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.MenuBar)) {
            if (ImGui.beginMenuBar()) {
                if (ImGui.beginMenu("Mod")) {
                    if (ImGui.menuItem("Client Info", "")) {
                        module = null;
                    }
                    if (ImGui.menuItem("Config Reload", "")) {
                        PigMode.getDefault().getConfigManager().reloadClient();
                    }
                    if (ImGui.menuItem("Config Save", "")) {
                        PigMode.getDefault().getConfigManager().unload();
                    }
                    ImGui.endMenu();
                }
                if (ImGui.beginMenu("FakePlayer")) {
                    if (ImGui.menuItem("Create", "")) {
                        PigMode.getDefault().getModuleManager().get(FakePlayer.class).enable();
                    }
                    if (ImGui.menuItem("Delete", "")) {
                        PigMode.getDefault().getModuleManager().get(FakePlayer.class).disable();
                    }
                    ImGui.endMenu();
                }
                ImGui.endMenuBar();
            }

            if (ImGui.beginTabBar("category", ImGuiTabBarFlags.None)) {
                for (Module.Category category : Module.Category.values()) {
                    if (ImGui.beginTabItem(category.getName())) {
                        this.category = category.getName();
                        ImGui.endTabItem();
                    }
                }
                ImGui.endTabBar();
            }

            if (ImGui.beginChild("modules", 150, 0, true)) {

                ArrayList<Module> modules = PigMode.getDefault().getModuleManager().stream().sorted(Comparator.comparing(m -> -mc.textRenderer.getWidth(m.getLabel()))).collect(Collectors.toCollection(ArrayList::new));
                for (Module m : modules) {
                    if (m.getCategory().getName().equalsIgnoreCase(category)) {
                        if (m.isToggled())
                            ImGui.textColored(0.0178f, 0.670f, 0.00670f, 1.f, "*");
                        else
                            ImGui.textColored(0.810f, 0.0162f, 0.0162f, 1.f, "*");

                        ImGui.sameLine();
                        if (ImGui.selectable(m.getLabel())) {
                            System.out.println(m.getLabel());
                            module = m;
                        }
                    }
                }
            }

            ImGui.endChild();
            ImGui.sameLine();
            ImGui.beginGroup();
            ImGui.beginChild("editor", 0, -ImGui.getFrameHeightWithSpacing());

            if (module != null) {
                ImGui.text(module.getLabel());

                ImGui.sameLine();
                ImGui.checkbox("", enabled.get(module));

                ImGui.sameLine();
                ImGui.button(KeyUtil.getKeyName(module.getKey()));

                if (ImGui.isItemClicked()) {
                    binding = true;
                }

                ImGui.sameLine();
                if (binding) {
                    ImGui.text("Press key");
                } else {
                    ImGui.text("Key");
                }

                ImGui.textWrapped("Description: " + module.getDescription());
                ImGui.separator();

                for (Option<?> o : PigMode.getDefault().getOptions()) {
                    if (!o.getFeature().equals(module)) continue;
                    if (o instanceof BooleanOption) {
                        boolean value = ((BooleanOption) o).getValue();
                        boolean updatedValue = value;
                        if (ImGui.checkbox(((BooleanOption) o).getLabel(), value)) updatedValue = !value;
                        if (updatedValue != value) ((BooleanOption) o).setValue(updatedValue);
                    } else if (o instanceof NumberOption) {
                        float[] valueArray = new float[]{((NumberOption) o).getValue().floatValue()};
                        ImGui.sliderFloat(o.getLabel(), valueArray, ((NumberOption) o).getMin(), ((NumberOption) o).getMax());
                        if (((NumberOption) o).getValue().floatValue() != valueArray[0]) {
                            ((NumberOption) o).setValue(valueArray[0]);
                        }
                    } else if (o instanceof ComboOption) {
                        String currentValue = ((ComboOption) o).getValue();
                        String[] comboItems = ((ComboOption) o).getCombo().toArray(new String[0]);
                        int currentItemIndex = -1;
                        for (int i = 0; i < comboItems.length; i++) {
                            if (comboItems[i].equals(currentValue)) {
                                currentItemIndex = i;
                                break;
                            }
                        }
                        if (ImGui.beginCombo(((ComboOption) o).getLabel(), comboItems[currentItemIndex])) {
                            for (int i = 0; i < comboItems.length; i++) {
                                boolean isSelected = (i == currentItemIndex);
                                if (ImGui.selectable(comboItems[i], isSelected)) {
                                    currentItemIndex = i;
                                    ((ComboOption) o).setValue(comboItems[i]);
                                }
                                if (isSelected) {
                                    ImGui.setItemDefaultFocus();
                                }
                            }
                            ImGui.endCombo();
                        }
                    } else if (o instanceof ColorOption) {
                        float[] color = ((ColorOption) o).asArray();
                        ImGui.colorEdit4(((ColorOption) o).getLabel(), color);
                        ((ColorOption) o).setValue(color);
                    } else if (o instanceof SeparatorOption){
                        ImGui.separator();
                    }
                }

                if (enabled.get(module).get() != module.isToggled()) module.toggle();
            } else {
                ImGui.text(PigMode.WATERMARK);
                ImGui.text("Prefix: " + CommandManager.getPrefix());
                ImGui.text("Developer: PigHax");
            }

            ImGui.endChild();
            ImGui.endGroup();
        }

        //-------------------------- END GUI

        ImGui.end();
        ImGui.render();
        implGl3.renderDrawData(ImGui.getDrawData());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
        }

        if (binding && module != null) {
            if (keyCode == GLFW.GLFW_KEY_DELETE) {
                module.setKey(-1);
            } else module.setKey(keyCode);
            binding = false;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (module != null)
            PigMode.getDefault().getModuleManager().get(ClickGui.class).savedModule = module;

        super.close();
        PigMode.getDefault().getModuleManager().get(ClickGui.class).setToggled(false);
    }

    private void setupStyle() {
        ImGui.styleColorsDark();
        ImGuiStyle style = ImGui.getStyle();
        style.setColor(ImGuiCol.Text, 0.87f, 0.87f, 0.87f, 1.00f);
        style.setColor(ImGuiCol.TextDisabled, 0.50f, 0.50f, 0.50f, 1.00f);
        style.setColor(ImGuiCol.WindowBg, 0.06f, 0.06f, 0.06f, 0.94f);
        style.setColor(ImGuiCol.ChildBg, 0.18f, 0.17f, 0.17f, 0.00f);
        style.setColor(ImGuiCol.PopupBg, 0.01f, 0.01f, 0.01f, 0.94f);
        style.setColor(ImGuiCol.Border, 0.42f, 0.42f, 0.42f, 0.50f);
        style.setColor(ImGuiCol.BorderShadow, 0.13f, 0.13f, 0.13f, 0.00f);
        style.setColor(ImGuiCol.FrameBg, 0.23f, 0.23f, 0.23f, 0.54f);
        style.setColor(ImGuiCol.FrameBgHovered, 0.28f, 0.28f, 0.28f, 0.40f);
        style.setColor(ImGuiCol.FrameBgActive, 0.22f, 0.22f, 0.22f, 0.67f);
        style.setColor(ImGuiCol.TitleBg, 0.18f, 0.18f, 0.18f, 1.00f);
        style.setColor(ImGuiCol.TitleBgActive, 0.26f, 0.26f, 0.27f, 1.00f);
        style.setColor(ImGuiCol.TitleBgCollapsed, 0.52f, 0.38f, 0.38f, 0.51f);
        style.setColor(ImGuiCol.MenuBarBg, 0.14f, 0.14f, 0.14f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarBg, 0.02f, 0.02f, 0.02f, 0.53f);
        style.setColor(ImGuiCol.ScrollbarGrab, 0.31f, 0.31f, 0.31f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarGrabHovered, 0.41f, 0.41f, 0.41f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarGrabActive, 0.51f, 0.51f, 0.51f, 1.00f);
        style.setColor(ImGuiCol.CheckMark, 1.00f, 1.00f, 1.00f, 1.00f);
        style.setColor(ImGuiCol.SliderGrab, 1.00f, 1.00f, 1.00f, 1.00f);
        style.setColor(ImGuiCol.SliderGrabActive, 0.75f, 0.75f, 0.75f, 1.00f);
        style.setColor(ImGuiCol.Button, 0.27f, 0.27f, 0.27f, 0.40f);
        style.setColor(ImGuiCol.ButtonHovered, 0.31f, 0.31f, 0.31f, 0.70f);
        style.setColor(ImGuiCol.ButtonActive, 0.25f, 0.25f, 0.25f, 1.00f);
        style.setColor(ImGuiCol.Header, 0.31f, 0.31f, 0.31f, 0.31f);
        style.setColor(ImGuiCol.HeaderHovered, 0.32f, 0.32f, 0.32f, 0.62f);
        style.setColor(ImGuiCol.HeaderActive, 0.32f, 0.32f, 0.32f, 0.70f);
        style.setColor(ImGuiCol.Separator, 0.43f, 0.43f, 0.50f, 0.50f);
        style.setColor(ImGuiCol.SeparatorHovered, 0.10f, 0.40f, 0.75f, 0.78f);
        style.setColor(ImGuiCol.SeparatorActive, 0.10f, 0.40f, 0.75f, 1.00f);
        style.setColor(ImGuiCol.ResizeGrip, 0.68f, 0.68f, 0.68f, 0.20f);
        style.setColor(ImGuiCol.ResizeGripHovered, 0.64f, 0.64f, 0.64f, 0.67f);
        style.setColor(ImGuiCol.ResizeGripActive, 0.48f, 0.48f, 0.48f, 0.95f);
        style.setColor(ImGuiCol.Tab, 0.21f, 0.21f, 0.21f, 0.86f);
        style.setColor(ImGuiCol.TabHovered, 0.35f, 0.35f, 0.35f, 0.80f);
        style.setColor(ImGuiCol.TabActive, 0.41f, 0.41f, 0.41f, 1.00f);
        style.setColor(ImGuiCol.TabUnfocused, 0.07f, 0.10f, 0.15f, 0.97f);
        style.setColor(ImGuiCol.TabUnfocusedActive, 0.14f, 0.26f, 0.42f, 1.00f);
        style.setColor(ImGuiCol.DockingPreview, 0.26f, 0.59f, 0.98f, 0.70f);
        style.setColor(ImGuiCol.DockingEmptyBg, 0.20f, 0.20f, 0.20f, 1.00f);
        style.setColor(ImGuiCol.PlotLines, 0.61f, 0.61f, 0.61f, 1.00f);
        style.setColor(ImGuiCol.PlotLinesHovered, 1.00f, 0.43f, 0.35f, 1.00f);
        style.setColor(ImGuiCol.PlotHistogram, 0.90f, 0.70f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.PlotHistogramHovered, 1.00f, 0.60f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.TableHeaderBg, 0.19f, 0.19f, 0.20f, 1.00f);
        style.setColor(ImGuiCol.TableBorderStrong, 0.31f, 0.31f, 0.35f, 1.00f);
        style.setColor(ImGuiCol.TableBorderLight, 0.23f, 0.23f, 0.25f, 1.00f);
        style.setColor(ImGuiCol.TableRowBg, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(ImGuiCol.TableRowBgAlt, 1.00f, 1.00f, 1.00f, 0.06f);
        style.setColor(ImGuiCol.TextSelectedBg, 0.26f, 0.59f, 0.98f, 0.35f);
        style.setColor(ImGuiCol.DragDropTarget, 1.00f, 1.00f, 0.00f, 0.90f);
        style.setColor(ImGuiCol.NavHighlight, 0.26f, 0.59f, 0.98f, 1.00f);
        style.setColor(ImGuiCol.NavWindowingHighlight, 1.00f, 1.00f, 1.00f, 0.70f);
        style.setColor(ImGuiCol.NavWindowingDimBg, 0.80f, 0.80f, 0.80f, 0.20f);
        style.setColor(ImGuiCol.ModalWindowDimBg, 0.80f, 0.80f, 0.80f, 0.35f);

        ImGui.getStyle().setFrameRounding(3);
        ImGui.getStyle().setGrabRounding(12);
        ImGui.getStyle().setTabRounding(4);
        ImGui.getStyle().setLogSliderDeadzone(4);
        ImGui.getStyle().setWindowMenuButtonPosition(1);
        ImGui.getStyle().setFrameBorderSize(1);
        ImGui.getStyle().setWindowRounding(5);
    }

}
