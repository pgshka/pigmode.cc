package me.mixins;

import me.client.PigMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( MinecraftClient.class )
public class MixinMinecraft {
    @Shadow private boolean windowFocused;

    @Inject(method = "stop", at = @At ("HEAD"))
    public void stop(CallbackInfo info) {
        PigMode.getDefault().getConfigManager().unload();
    }
}