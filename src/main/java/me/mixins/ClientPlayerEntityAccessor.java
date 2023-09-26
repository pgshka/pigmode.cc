package me.mixins;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerEntity.class)
public interface ClientPlayerEntityAccessor {
    @Accessor("renderYaw") float renderYaw();
    @Accessor("renderYaw") void setRenderYaw(float yaw);
    @Accessor("renderPitch") float renderPitch();
    @Accessor("renderPitch") void setRenderPitch(float renderPitch);

}