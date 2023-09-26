package me.client.impl.modules.anarchy;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.PlaySoundEvent;
import me.client.api.events.PlayerMoveEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.NumberOption;
import me.client.impl.Module;
import me.mixininterface.IVec3d;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Vec3d;

public final class ElytraFly extends Module {

    public ElytraFly() {
        super("ElytraFly", "elytra fly for constantiam", Category.ANARCHY);
    }

    float y;

    NumberOption horizontalSpeed = new NumberOption.Builder(1.8).name("Horizontal Speed").setBounds(0, 2F).build(this);
    NumberOption verticalSpeed = new NumberOption.Builder(0.5).name("Vertical Speed").setBounds(0, 0.8F).build(this);
    NumberOption fallSpeed = new NumberOption.Builder(0.05).name("Fall Speed").setBounds(0, 0.05F).build(this);
    BooleanOption unloadedChunkStop = new BooleanOption.Builder(true).name("Unloaded Chunk Stop").build(this);
    BooleanOption waterStop = new BooleanOption.Builder(true).name("Water Stop").build(this);

    @Subscribe
    public void onMove(PlayerMoveEvent event) {
        if (nullCheck()) return;
        float speed = horizontalSpeed.getValue().floatValue();
        if (mc.player.isFallFlying()) {

            if (mc.player.isTouchingWater() && waterStop.getValue()) {
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
                return;
            }

            double forward = mc.player.input.movementForward;
            double strafe = mc.player.input.movementSideways;
            float yaw = mc.player.getYaw();

            if (mc.options.jumpKey.isPressed())
                y = verticalSpeed.getValue().floatValue();
            else if (mc.options.sneakKey.isPressed())
                y = -verticalSpeed.getValue().floatValue();
            else
                y = -fallSpeed.getValue().floatValue();

            if (forward == 0.0 && strafe == 0.0) {
                ((IVec3d) event.movement).set(0, y, 0);
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }

                double x = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                double z = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));

                ((IVec3d) event.movement).set(x, y, z);

                int chunkX = (int) ((mc.player.getX() + event.movement.x) / 16);
                int chunkZ = (int) ((mc.player.getZ() + event.movement.z) / 16);

                if (!mc.world.getChunkManager().isChunkLoaded(chunkX, chunkZ) && unloadedChunkStop.getValue())
                    ((IVec3d) event.movement).set(0, 0, 0);
            }
        }
    }
}