package me.client.impl.modules.legit;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.PacketEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.ComboOption;
import me.client.api.option.impl.NumberOption;
import me.client.api.utils.PlayerInterUtil;
import me.client.api.utils.Timer;
import me.client.impl.Module;
import me.mixins.MinecraftClientAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CrystalBreak extends Module {

    public CrystalBreak() {
        super("CrystalBreak", "Legit autocrystal for vanilla servers", Category.LEGIT);
    }
    BooleanOption itemCheck = new BooleanOption.Builder(true).name("Item Check").build(this);
    BooleanOption attack  = new BooleanOption.Builder(true).name("Attack").build(this);
    BooleanOption setDead = new BooleanOption.Builder(true).name("Set Dead").build(this);
    NumberOption speed = new NumberOption.Builder(2).name("Speed").setBounds(0,5).build(this);
    public ComboOption clearMethod = new ComboOption.Builder("After Death").name("Crystals Clear Method").setCombo(List.of("After Attack", "After Death")).build(this);

    Set<BlockPos> placedCrystals = new HashSet<>();
    Timer timer = new Timer();

    public boolean checkCrystal(BlockPos position) {

        for (BlockPos placedCrystal : placedCrystals) {
            if (position.isWithinDistance(placedCrystal, 1.5)) {
                return true;
            }
        }

        return false;
    }

    public void onToggle() {
        timer.reset();
        placedCrystals.clear();
    }

    @Override
    public void tick(){
        if (mc.crosshairTarget instanceof EntityHitResult
                && ((EntityHitResult) mc.crosshairTarget).getEntity() instanceof EndCrystalEntity
                && (!itemCheck.getValue()
                || mc.player.isHolding(Items.END_CRYSTAL))
                && checkCrystal(((EntityHitResult) mc.crosshairTarget).getEntity().getBlockPos())
                && attack.getValue()) {
            if (timer.passed(10 + (int) (Math.random() * (5 / speed.getValue().floatValue()) * 150))) {
                ((MinecraftClientAccessor) mc).attack();
                if (clearMethod.getValue().equalsIgnoreCase("After Attack")) placedCrystals.clear();
                timer.reset();
            }
        }
    }
    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event){
        if (event.getPacket() instanceof DeathMessageS2CPacket packet) {
            Entity entity = mc.world.getEntityById(packet.getEntityId());
            if (entity == mc.player && clearMethod.getValue().equalsIgnoreCase("After Death"))
                placedCrystals.clear();
        }
    }
    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket && setDead.getValue()) {
            PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket) event.getPacket();
            if (PlayerInterUtil.getInteractType(packet) == PlayerInterUtil.InteractType.ATTACK && PlayerInterUtil.getEntity(packet) instanceof LivingEntity) {
                Entity entity = PlayerInterUtil.getEntity(packet);
                if (checkCrystal(entity.getBlockPos())) ((EntityHitResult) mc.crosshairTarget).getEntity().kill();
            }
        }

        //add crystal position to list for only own break
        if (event.getPacket() instanceof PlayerInteractBlockC2SPacket) {
            PlayerInteractBlockC2SPacket p = (PlayerInteractBlockC2SPacket) event.getPacket();
            Block block = mc.world.getBlockState(p.getBlockHitResult().getBlockPos()).getBlock();
            if ((block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) && mc.player.getMainHandStack().getItem() instanceof EndCrystalItem) {
                placedCrystals.add(p.getBlockHitResult().getBlockPos());
            }
        }
    }
}