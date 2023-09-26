package me.client.impl.modules.client;

import com.google.common.eventbus.Subscribe;
import me.client.PigMode;
import me.client.api.events.Render2DEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.ColorOption;
import me.client.api.option.impl.ComboOption;
import me.client.api.option.impl.NumberOption;
import me.client.api.utils.text.TextRenderer;
import me.client.impl.Module;
import me.client.impl.ui.GuiNew;
import me.x150.renderer.render.Renderer2d;
import me.x150.renderer.render.Renderer3d;
import org.lwjgl.glfw.GLFW;


import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class Hud extends Module {
    public Module savedModule = null;
    public Hud() {
        super("Hud", "hud", Category.CLIENT);
    }

    public ColorOption color = new ColorOption.Builder(new Color(255, 255, 255,255)).name("Color").build(this);
    public BooleanOption watermark = new BooleanOption.Builder(false).name("Watermark").build(this);
    public BooleanOption featurelist = new BooleanOption.Builder(false).name("Feature List").build(this);

    @Subscribe
    private void onRender2D(Render2DEvent event) {
        TextRenderer text = TextRenderer.get();

        int scaledWidth = mc.getWindow().getScaledWidth();
        int scaledHeight = mc.getWindow().getScaledHeight();

        int ofs = 1; int offsets = 1;

        Renderer2d.renderLine(event.drawContext.getMatrices(), color.getValue(), 0,0,10,2);

        if (watermark.getValue()) text.render(PigMode.WATERMARK, 1,1, color.getValue());

        if (featurelist.getValue()) {
            ArrayList<Module> modules = PigMode.getDefault().getModuleManager().stream().sorted(Comparator.comparing(m -> -text.getWidth(m.getLabel()))).collect(Collectors.toCollection(ArrayList::new));
            int y = 0, count = 1;
            for (Module module : modules) {
                if (!module.isToggled()) continue;
                text.render(module.getLabel(), scaledWidth - text.getWidth(module.getLabel()) - 2, y + 1, color.getValue());
                y += (int) (text.getHeight() + offsets);
                count++;
            }
        }
    }

}