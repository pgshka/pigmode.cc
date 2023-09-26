package me.client.impl.modules.anarchy;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.Render3DEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.ColorOption;
import me.client.api.option.impl.NumberOption;
import me.client.api.utils.*;
import me.client.impl.Module;
import me.x150.renderer.render.Renderer3d;
import me.x150.renderer.util.RendererUtils;
import net.minecraft.block.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class BedAura extends Module {
    public Module savedModule = null;

    public BedAura() {
        super("BedAura", "description", Category.ANARCHY);
    }

    private PlayerEntity closestTarget;
    private BlockPos toBreak, toRender;
    private boolean disableModule;
    private int tick;
    private short rotYaw;

    NumberOption range = new NumberOption.Builder(4).name("Range").setBounds(0, 8).build(this);
    NumberOption delay = new NumberOption.Builder(4).name("Delay").setBounds(0, 20).build(this);
    BooleanOption breakBed = new BooleanOption.Builder(true).name("Break Bed").build(this);
    BooleanOption rotate = new BooleanOption.Builder(true).name("Rotate").build(this);

    private PlayerEntity findTarget() {
        List<AbstractClientPlayerEntity> players = mc.world.getPlayers();
        players.sort(Comparator.comparing(e -> e.getHealth() + e.getAbsorptionAmount()));
        for (AbstractClientPlayerEntity e : players) {
            if (mc.player.distanceTo(e) <= range.getValue().intValue() && e != mc.player && e.isAlive() && e.age > 20)
                return (PlayerEntity) e;
        }
        return null;
    }

    public List<BlockPos> getPlacePositon(BlockPos pos) {
        List<BlockPos> maybe = new ArrayList<>();
        BlockPos posForCheck = pos;

        if (mc.world.getBlockState(posForCheck).getBlock() instanceof BedBlock){
            doBreak(posForCheck);
            return null;
        }
        if (mc.world.getBlockState(posForCheck.up()).getBlock() instanceof BedBlock){
            doBreak(posForCheck.up());
            return null;
        }

        if (canPlaceBed(posForCheck.south()))
            maybe.add(posForCheck.south());
        if (canPlaceBed(posForCheck.west()))
            maybe.add(posForCheck.west());
        if (canPlaceBed(posForCheck.north()))
            maybe.add(posForCheck.north());
        if (canPlaceBed(posForCheck.east()))
            maybe.add(posForCheck.east());

        return maybe;
    }

    private boolean doBreak(BlockPos pos) {
        if (pos == null) return false;
        if (!mc.world.getBlockState(pos).hasBlockEntity()) {
            toBreak = null;
            return false;
        }

        boolean prev = mc.player.isSneaking();
        if (prev)
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        if (rotate.getValue()) RotateUtils.rotate(pos);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(pos.toCenterPos(), Direction.UP, pos, false));
        if (prev)
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        toBreak = null;
        return true;
    }

    private boolean swapToBed(){
        if (ItemUtil.getItemSlot(BedItem.class) != -1) {
            ItemUtil.swapSlot(ItemUtil.getItemSlot(BedItem.class));
            return true;
        }
        return false;
    }

    private void doPlace(BlockPos playerTargetPos) {
        if (swapToBed()) {} else {
            ChatUtil.send(getLabel() + " - Disabled, because you dont have beds in your hotbars");
            disableModule = true;
        }

        BlockPos placeTarget;
        BlockPos playerTarget = playerTargetPos;
        List<BlockPos> placeList = getPlacePositon(playerTarget);

        if (placeList == null) return;

        if (placeList.isEmpty()) {
            placeList = getPlacePositon(playerTargetPos.up());
            playerTarget = playerTargetPos.up();
        }
        if (!placeList.isEmpty()) {
            placeList.sort(Comparator.comparing(pos -> mc.player.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ())));
            placeTarget = placeList.get(0);
        } else return;

        rotYaw = 180;
        if (playerTarget.equals(placeTarget.east())) rotYaw = -90;
        else if (playerTarget.equals(placeTarget.west())) rotYaw = 90;
        else if (playerTarget.equals(placeTarget.south())) rotYaw = 0;

        if (rotate.getValue())
            RotateUtils.rotate(placeTarget);

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), rotYaw, mc.player.getPitch(mc.getTickDelta()), mc.player.isOnGround()));
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(placeTarget.toCenterPos(), Direction.UP, placeTarget, true));

        toBreak = placeTarget;
        toRender = placeTarget;
    }

    private boolean canPlaceBed(BlockPos pos) {
        if (!(mc.world.getBlockState(pos).getBlock() instanceof AirBlock
                || mc.world.getBlockState(pos).getBlock() instanceof FireBlock)) return false;
        if (mc.world.getBlockState(pos.down()).getBlock() instanceof AirBlock) return false;
        if (mc.world.getBlockState(pos.down()).getBlock() instanceof FireBlock) return false;
        if (!mc.world.getOtherEntities(null, new Box(pos)).isEmpty()) return false;
        return true;
    }

    @Override
    public void tick() {
        if (nullCheck()) return;

        if (disableModule) setToggled(false);

        closestTarget = findTarget();
        if (closestTarget != null) {
            if (toBreak == null)
                doPlace(closestTarget.getBlockPos());
            if (tick >= delay.getValue().intValue() && toBreak != null) {
                if (breakBed.getValue()) doBreak(toBreak);
                tick = 0;
            }
        }
        tick++;
    }

    @Subscribe
    public void onRender(Render3DEvent event) {
        if (nullCheck() || closestTarget == null || toRender == null) {
            System.out.println("null +" + tick);
            return;
        }
        Renderer3d.renderThroughWalls();
        Vec3d pos = closestTarget.getBlockPos().toCenterPos();
        Renderer3d.renderLine(event.getMatrices(), new Color(255, 255, 255, 255), RendererUtils.getCrosshairVector(), pos);
        Renderer3d.renderLine(event.getMatrices(), new Color(255, 0, 0, 255), closestTarget.getBlockPos().toCenterPos(), toRender.toCenterPos());
        Renderer3d.stopRenderThroughWalls();
    }

    @Override
    public void onDisable() {
        toBreak = null; toRender = null;
        tick = 0; rotYaw = 0;
        disableModule = false;
    }

    @Override
    public void onEnable() {
        if (ItemUtil.getItemSlot(BedItem.class) == -1){
            ChatUtil.send(getLabel() + " - Disabled, because you dont have beds in your hotbars");
            disableModule = true;
        }
    }
}