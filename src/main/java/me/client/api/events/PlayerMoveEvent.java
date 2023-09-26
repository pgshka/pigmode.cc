package me.client.api.events;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class PlayerMoveEvent {
    public MovementType type;
    public Vec3d movement;
    public PlayerMoveEvent(MovementType type, Vec3d movement) {
        this.type = type;
        this.movement = movement;
    }
}