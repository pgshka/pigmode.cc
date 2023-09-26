package me.client.impl.modules.global;

import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import me.client.api.events.PacketEvent;
import me.client.api.option.impl.NumberOption;
import me.client.impl.Module;
import me.mixins.MinecraftClientAccessor;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;

import java.util.UUID;

public final class FakePlayer extends Module {

    public FakePlayer() {
        super("FakePlayer", "fake player", Category.GLOBAL);
    }

    OtherClientPlayerEntity fakePlayer;

    @Override
    public void onEnable(){
        if (nullCheck()) return;
        fakePlayer = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.fromString("e1964300-7751-4630-9c53-4d2a8767ed45"), "fakeplayer-x0598"));
        fakePlayer.copyPositionAndRotation(mc.player);
        fakePlayer.setHealth(20);
        mc.world.addEntity(-101, fakePlayer);
    }

    @Override
    public void onDisable(){
        if (fakePlayer != null) fakePlayer.remove(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public void tick() {
        if (nullCheck()) toggle();
    }
}