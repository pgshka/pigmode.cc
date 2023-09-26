package me.mixins;

import me.client.PigMode;
import me.client.api.events.Render3DEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow public abstract void updateTargetedEntity(float tickDelta);

    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = { "ldc=hand" }), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.world == null || mc.player == null) return;

        mc.getProfiler().push("pigmode-client_render");

        Render3DEvent event = new Render3DEvent(matrices, tickDelta, mc.getCameraEntity().getPos().x, mc.getCameraEntity().getPos().y, mc.getCameraEntity().getPos().z);

        PigMode.EVENT_BUS.post(event);
        RenderSystem.applyModelViewMatrix();
        mc.getProfiler().pop();
    }
}