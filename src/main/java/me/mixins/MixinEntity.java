package me.mixins;

import me.client.PigMode;
import me.client.api.Globals;
import me.client.api.events.PacketEvent;
import me.client.api.events.PlayerMoveEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.client.api.Globals.mc;

@Mixin(Entity.class)
public abstract class MixinEntity {


    @Inject(method = "move", at = @At("HEAD"))
    private void onMove(MovementType type, Vec3d movement, CallbackInfo info) {
        if ((Object) this == mc.player) {
            PlayerMoveEvent event = new PlayerMoveEvent(type, movement);
            PigMode.EVENT_BUS.post(event);
        }
    }
}