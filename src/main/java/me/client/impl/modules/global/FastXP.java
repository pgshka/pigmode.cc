package me.client.impl.modules.global;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.PacketEvent;
import me.client.api.option.impl.NumberOption;
import me.client.impl.Module;
import me.mixins.MinecraftClientAccessor;
import net.minecraft.item.Items;

public final class FastXP extends Module {

    public FastXP() {
        super("FastXP", "fast xp?", Category.GLOBAL);
    }

    NumberOption speed = new NumberOption.Builder(0.4).name("Speed").setBounds(0,4).build(this);
    @Override public void tick() {
        if (nullCheck()) return;
        if (mc.player.isHolding(Items.EXPERIENCE_BOTTLE)) {
            ((MinecraftClientAccessor) mc).setCooldown((int) Math.min(((MinecraftClientAccessor) mc).getCooldown(), speed.getValue().intValue()));
        }
    }
}