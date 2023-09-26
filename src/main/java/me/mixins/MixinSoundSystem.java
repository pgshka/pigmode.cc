package me.mixins;

import me.client.PigMode;
import me.client.api.events.PacketEvent;
import me.client.api.events.PlaySoundEvent;
import net.minecraft.client.sound.ElytraSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SoundSystem.class)
public class MixinSoundSystem {

    @Shadow @Final private Map<SoundInstance, Integer> soundEndTicks;

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    private void onPlay(SoundInstance soundInstance, CallbackInfo info) {
        PlaySoundEvent event = new PlaySoundEvent(soundInstance);
        PigMode.EVENT_BUS.post(event);
        if (event.isCancelled()) info.cancel();
    }
}