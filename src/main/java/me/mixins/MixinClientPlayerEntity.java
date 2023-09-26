package me.mixins;

import me.client.PigMode;
import me.client.api.Globals;
import me.client.impl.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity implements Globals {

    @Inject(method = "tick", at = @At("RETURN"), cancellable = true)
    public void tick(CallbackInfo info) {
        for (Module m : PigMode.getDefault().getModuleManager()) {
            if (MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().player != null && m.isToggled()) {
                m.tick();
            }
        }
    }

}