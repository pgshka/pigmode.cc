package me.client.impl.modules.global;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.PacketEvent;
import me.client.api.option.impl.BooleanOption;
import me.client.api.option.impl.ComboOption;
import me.client.api.option.impl.NumberOption;
import me.client.api.utils.ItemUtil;
import me.client.api.utils.RotateUtils;
import me.client.api.utils.Timer;
import me.client.impl.Module;
import me.mixins.MinecraftClientAccessor;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public final class MiddleClick extends Module {

    public MiddleClick() {
        super("MiddleClick", "MiddleClick/Pearl/Xp", Category.GLOBAL);
    }

    ComboOption mode = new ComboOption.Builder("XP").name("Mode").setCombo(List.of("XP", "Pearl")).build(this);
    NumberOption delay = new NumberOption.Builder(0).name("XP Delay").setBounds(0, 20).build(this);
    BooleanOption feet = new BooleanOption.Builder(true).name("Feet").build(this);
    boolean isHolding;
    Timer xpDelay = new Timer();

    @Override
    public void onToggle() {
        isHolding = false;
        xpDelay.reset();
    }

    @Override
    public void tick() {
        if (nullCheck()) return;
        if (isHolding) {
            if (mc.crosshairTarget != null) mc.crosshairTarget = null;

            int oldSlot = mc.player.getInventory().selectedSlot;
            int toSlot = ItemUtil.getItemSlot(
                    mode.getValue().equals("XP") ? Items.EXPERIENCE_BOTTLE : Items.ENDER_PEARL);

            if (mode.getValue().equalsIgnoreCase("XP")) {
                if (xpDelay.passed(delay.getValue().longValue() * 10)) {
                    ItemUtil.swapSlot(toSlot);
                    if (feet.getValue()) RotateUtils.rotate(mc.player.getYaw(), 0);
                    ((MinecraftClientAccessor) mc).interact();
                    ItemUtil.swapSlot(oldSlot);
                    xpDelay.reset();
                }
            } else {
                if (mc.player.getItemCooldownManager().getCooldownProgress(Items.ENDER_PEARL, 0.0F) == 0) {
                    ItemUtil.swapSlot(toSlot);
                    ((MinecraftClientAccessor) mc).interact();
                    ItemUtil.swapSlot(oldSlot);
                }
            }
        }
        isHolding = GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 2) == GLFW.GLFW_PRESS;
    }
}