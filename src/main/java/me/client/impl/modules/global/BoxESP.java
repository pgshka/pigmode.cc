package me.client.impl.modules.global;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.Render3DEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.ColorOption;
import me.client.api.utils.Utils;
import me.client.impl.Module;
import me.x150.renderer.render.Renderer3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public final class BoxESP extends Module {

    public BoxESP() {
        super("BoxESP", "bot esp only for players", Category.GLOBAL);
    }
    ColorOption boxColor = new ColorOption.Builder(new Color(255, 0, 0,255)).name("Box Color").build(this);
    ColorOption outlineColor = new ColorOption.Builder(new Color(255, 0, 0,255)).name("Outline Color").build(this);
    BooleanOption renderThroughWalls = new BooleanOption.Builder(true).name("Render Through Walls").build(this);

    public static Vec3d getBoundingBoxDimensions(Box boundingBox) {
        double width = Math.abs(boundingBox.maxX - boundingBox.minX);
        double height = Math.abs(boundingBox.maxY - boundingBox.minY);
        double depth = Math.abs(boundingBox.maxZ - boundingBox.minZ);

        return new Vec3d(width, height, depth);
    }

    @Subscribe
    public void onRender(Render3DEvent event){
        if (nullCheck()) return;
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity) {
                if (entity.equals(mc.player)) continue;
                Vec3d pos = Utils.getInterpolatedEntityPosition(entity);
                if (renderThroughWalls.getValue()) Renderer3d.renderThroughWalls();
                Renderer3d.renderEdged(event.getMatrices(), boxColor.getValue(), outlineColor.getValue(), pos.subtract(new Vec3d(entity.getWidth(), 0, entity.getWidth()).multiply(0.5)), getBoundingBoxDimensions(entity.getBoundingBox()));
                if (renderThroughWalls.getValue()) Renderer3d.stopRenderThroughWalls();
            }
        }
    }
}
