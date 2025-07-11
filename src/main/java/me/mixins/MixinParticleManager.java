package me.mixins;

import me.client.PigMode;
import me.client.api.events.EventTickParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class MixinParticleManager {

    @Inject(method = "tickParticle", at = @At("HEAD"), cancellable = true)
    public void tickParticle1(Particle particle, CallbackInfo ci) {
        EventTickParticle eventTickParticle = new EventTickParticle(particle);
        PigMode.EVENT_BUS.post(eventTickParticle);
        if (eventTickParticle.isCancelled() && particle.isAlive()) {
            ci.cancel();
            particle.markDead();
        }
    }

    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    public void addParticle1(Particle particle, CallbackInfo ci) {
        EventTickParticle eventTickParticle = new EventTickParticle(particle);
        PigMode.EVENT_BUS.post(eventTickParticle);
        if (eventTickParticle.isCancelled() && particle.isAlive()) {
            ci.cancel();
            particle.markDead();
        }
    }
}