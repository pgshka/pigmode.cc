package me.mixins;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.net.Proxy;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Invoker("doAttack") boolean attack();
    @Invoker("doItemUse") void interact();

    @Accessor("itemUseCooldown") int getCooldown();

    @Accessor("itemUseCooldown") void setCooldown(int val);
}