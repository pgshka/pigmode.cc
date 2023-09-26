package me.mixins;

import me.client.PigMode;
import me.client.api.events.PlayerMoveEvent;
import me.client.api.events.Render2DEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( InGameHud.class )
public class MixinInGameHud {
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        Render2DEvent event = new Render2DEvent(context, scaledWidth, scaledHeight, tickDelta);
        PigMode.EVENT_BUS.post(event);
    }
}