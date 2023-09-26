package me.client.impl.modules.client;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.Render2DEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.ColorOption;
import me.client.api.option.impl.ComboOption;
import me.client.api.option.impl.NumberOption;
import me.client.impl.Module;
import me.client.impl.ui.GuiNew;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;

public final class ClickGui extends Module {
    public Module savedModule = null;
    public ClickGui() {
        super("ClickGui", "description", Category.CLIENT);
        setKey(GLFW.GLFW_KEY_INSERT);
    }

    public ColorOption color = new ColorOption.Builder(new Color(10,10,10,255)).name("Color").build(this);
    public BooleanOption demoGui = new BooleanOption.Builder(false).name("Demo Gui").build(this);
    public NumberOption speed = new NumberOption.Builder(42).name("Speed").setBounds(0,4).build(this);
    public NumberOption speed2 = new NumberOption.Builder(40).name("Speed 2").setBounds(0,4).build(this);
    public ComboOption comboOption = new ComboOption.Builder("PigHax").name("Combo").setCombo(List.of("PigHax", "W202", "PigHack", "Boguna", "LilGUGU", "Nvc")).build(this);
    @Override public void onEnable() {
        if (!(mc.currentScreen instanceof GuiNew)) mc.setScreen(new GuiNew());
        setToggled(false);
    }
}