package me.client.api.utils;

import me.client.api.Globals;
import me.mixins.ClientPlayerEntityAccessor;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotateUtils implements Globals {

    public static float startYaw, startPitch;
    public static float yaw, pitch;
    private static final Vec3d eyesPos = new Vec3d(mc.player.getX(),
            mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()),
            mc.player.getZ());

    public static void rotate(BlockPos bp) {
        startYaw = mc.player.getYaw();
        startPitch = mc.player.getPitch();
        Vec3d vec = new Vec3d(bp.getX(), bp.getY(), bp.getZ());
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        float[] rotations = {
                mc.player.getYaw()
                        + MathHelper.wrapDegrees(yaw - mc.player.getYaw()),
                mc.player.getPitch() + MathHelper
                        .wrapDegrees(pitch - mc.player.getPitch())};

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations[0], rotations[1], mc.player.isOnGround()));
    }

    public static void rotate(Entity e) {
        startYaw = mc.player.getYaw();
        startPitch = mc.player.getPitch();
        Vec3d vec = new Vec3d(e.getX(), e.getY(), e.getZ());
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        float[] rotations = {
                mc.player.getYaw()
                        + MathHelper.wrapDegrees(yaw - mc.player.getYaw()),
                mc.player.getPitch() + MathHelper
                        .wrapDegrees(pitch - mc.player.getPitch())};

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations[0], rotations[1], mc.player.isOnGround()));
    }

    public static void rotate(float yaw, float pitch) {
        ClientPlayerEntity player = mc.player;
        startYaw = mc.player.getYaw();
        startPitch = mc.player.getPitch();
        float[] rotations = {
                mc.player.getYaw()
                        + MathHelper.wrapDegrees(yaw - mc.player.getYaw()),
                mc.player.getPitch() + MathHelper
                        .wrapDegrees(pitch - mc.player.getPitch())};

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations[0], rotations[1], mc.player.isOnGround()));
    }
}