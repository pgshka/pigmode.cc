package me.client.impl.modules.global;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.Render3DEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.ColorOption;
import me.client.api.option.impl.NumberOption;
import me.client.api.utils.RotateUtils;
import me.client.api.utils.Utils;
import me.client.impl.Module;
import me.x150.renderer.render.Renderer3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public final class AntiAim extends Module {

    public AntiAim() {
        super("AntiAim", "csgo shit", Category.GLOBAL);
    }

    NumberOption yaw = new NumberOption.Builder(0).name("Yaw").setBounds(-180,180).build(this);
    NumberOption pitch = new NumberOption.Builder(0).name("Pithch").setBounds(-180,180).build(this);


    @Override
    public void onEnable(){
        setToggled(false);
    }
//    @Override
//    public void tick(){
//        if (nullCheck()) return;
//
//        RotateUtils.rotate(yaw.getValue().floatValue(), pitch.getValue().floatValue());
//    }
}
