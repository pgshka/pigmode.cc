package me.client.impl.modules.global;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.PacketEvent;
import me.client.api.events.Render3DEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.ColorOption;
import me.client.api.option.impl.NumberOption;
import me.client.api.option.impl.SeparatorOption;
import me.client.api.utils.Utils;
import me.client.api.utils.render.RenderUtil;
import me.client.impl.Module;
import me.mixins.MinecraftClientAccessor;
import me.x150.renderer.Renderer;
import me.x150.renderer.render.Renderer3d;
import me.x150.renderer.util.RendererUtils;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public final class Tracers extends Module {

    public Tracers() {
        super("Tracers", "tracker", Category.GLOBAL);
    }

    NumberOption distance = new NumberOption.Builder(100).name("Max Distance").setBounds(0, 200).build(this);


    SeparatorOption separator1 = new SeparatorOption.Builder("separator").name("Separator1").build(this);
    BooleanOption players = new BooleanOption.Builder(true).name("Players").build(this);
    ColorOption playersColor = new ColorOption.Builder(new Color(144, 255, 79, 255)).name("Players Color").build(this);


    SeparatorOption separator2 = new SeparatorOption.Builder("separator").name("Separator2").build(this);
    BooleanOption monsters = new BooleanOption.Builder(true).name("Monsters").build(this);
    ColorOption monstersColor = new ColorOption.Builder(new Color(255, 0, 0, 255)).name("Monsters Color").build(this);


    SeparatorOption separator3 = new SeparatorOption.Builder("separator").name("Separator3").build(this);
    BooleanOption animals = new BooleanOption.Builder(true).name("Animals").build(this);
    ColorOption animalsColor = new ColorOption.Builder(new Color(255, 217, 94, 255)).name("Animals Color").build(this);

    @Subscribe
    public void onRender(Render3DEvent event) {
        if (nullCheck()) return;
        for (Entity entity : mc.world.getEntities()) {
            if (mc.player.distanceTo(entity) > distance.getValue().intValue()) continue;
            if (entity.equals(mc.player)) continue;

            Vec3d pos = Utils.getInterpolatedEntityPosition(entity);
            Renderer3d.renderThroughWalls();

            if (entity instanceof PlayerEntity && players.getValue())
                Renderer3d.renderLine(event.getMatrices(), playersColor.getValue(), RendererUtils.getCrosshairVector(), pos.add(0, entity.getHeight() / 2, 0));


            if (entity instanceof Monster && monsters.getValue())
                Renderer3d.renderLine(event.getMatrices(), monstersColor.getValue(), RendererUtils.getCrosshairVector(), pos.add(0, entity.getHeight() / 2, 0));

            if ((entity instanceof AnimalEntity ||
                    entity instanceof VillagerEntity ||
                    entity instanceof AmbientEntity ||
                    entity instanceof WaterCreatureEntity ||
                    entity instanceof GolemEntity ||
                    entity instanceof AllayEntity)
                    && animals.getValue())
                Renderer3d.renderLine(event.getMatrices(), animalsColor.getValue(), RendererUtils.getCrosshairVector(), pos.add(0, entity.getHeight() / 2, 0));


            Renderer3d.stopRenderThroughWalls();
        }
    }
}