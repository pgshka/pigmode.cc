package me.client.impl.modules.legit;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.PacketEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.NumberOption;
import me.client.api.utils.Timer;
import me.client.impl.Module;
import me.mixins.MinecraftClientAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public final class AnchorAura extends Module {

    public AnchorAura() {
        super("AnchorAura", "description", Category.LEGIT);
    }

    NumberOption freeSlot = new NumberOption.Builder(6).name("Free Slot").setBounds(0,4).build(this);
    NumberOption randomDelay = new NumberOption.Builder(1).name("Random Delay (Interact)").setBounds(0,5).build(this);
    BooleanOption swapDelay = new BooleanOption.Builder(true).name("Swap Delay").build(this);

    Set<BlockPos> placedAnchor = new HashSet<>();
    Timer timer = new Timer();
    boolean breakAnchor = false;

    public boolean checkAnchor(BlockPos position){
        for (BlockPos placedAnchor : placedAnchor) {
            if (position.isWithinDistance(placedAnchor,1.5)){
                return true;
            }
        }
        return false;
    }

    public void onToggle(){
        placedAnchor.clear();
        breakAnchor = false;
        timer.reset();
    }

    @Override
    public void tick(){
        if (!(mc.crosshairTarget instanceof BlockHitResult)) return;
        BlockHitResult res = (BlockHitResult) mc.crosshairTarget;
        if (mc.world.getBlockState(res.getBlockPos()).getBlock().equals(Blocks.RESPAWN_ANCHOR)){
            if (checkAnchor(res.getBlockPos())) {
                if (!breakAnchor && timer.passed( swapDelay.getValue() ? ((long) (randomDelay.getValue().floatValue() * 85)) : 15)){
                    mc.player.getInventory().selectedSlot = freeSlot.getValue().intValue();
                    timer.reset();
                    breakAnchor = true;
                }
                if (breakAnchor && timer.passed(50 + (int) (Math.random() * randomDelay.getValue().floatValue() * 150))) {
                    ((MinecraftClientAccessor) mc).interact();
                    placedAnchor.clear();
                    breakAnchor = false;
                    timer.reset();
                }
            }
        }
    }

    @Subscribe public void onPacketSend(PacketEvent.Send event){
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket){
            PlayerInteractBlockC2SPacket p = (PlayerInteractBlockC2SPacket) event.getPacket();
            BlockState block = mc.world.getBlockState(p.getBlockHitResult().getBlockPos());
            if (block.getBlock() instanceof RespawnAnchorBlock && block.get(RespawnAnchorBlock.CHARGES) > 0) {
                placedAnchor.add(p.getBlockHitResult().getBlockPos());
            }
        }
    }
}