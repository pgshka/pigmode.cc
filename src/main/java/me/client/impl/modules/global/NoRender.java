package me.client.impl.modules.global;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.EventTickParticle;
import me.client.api.events.PacketEvent;
import me.client.api.option.impl.NumberOption;
import me.client.impl.Module;
import me.mixins.MinecraftClientAccessor;
import net.minecraft.client.particle.*;
import net.minecraft.item.Items;

public final class NoRender extends Module {

    public NoRender() {
        super("NoRender", "?", Category.GLOBAL);
    }

    @Subscribe
    public void noRender(EventTickParticle event) {
        if (event.getParticle() instanceof ExplosionSmokeParticle
                || event.getParticle() instanceof FireSmokeParticle
                || event.getParticle() instanceof CampfireSmokeParticle
                || event.getParticle() instanceof ExplosionLargeParticle
                || event.getParticle() instanceof ExplosionEmitterParticle
                || event.getParticle() instanceof FireworksSparkParticle.FireworkParticle
                || event.getParticle() instanceof BlockDustParticle
        ) {
            event.cancel();
        }
    }
}