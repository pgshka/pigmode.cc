package me.client.api.utils;

import me.client.api.Globals;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtil implements Globals {

    public static int getItemSlot(Item item){
        for (int i = 0; i < 9; i++){
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem().equals(item)) return i;
        }
        return -1;
    }
    public static int getItemSlot(Class clazz) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (clazz.isInstance(stack.getItem())) return i;
        }
        return -1;
    }

    public static int getItemSlot(Block item){
        for (int i = 0; i < 9; i++){
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) {
                if (((BlockItem) mc.player.getInventory().getStack(i).getItem()).getBlock() == item) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int getItemSlotInventory(Item item){
        for (int i = 0; i < 36; i++){
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem().equals(item)) return i;
        }
        return -1;
    }
    public static int getItemSlotInventory(Class clazz) {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (clazz.isInstance(stack.getItem())) return i;
        }
        return -1;
    }

    public static int getItemSlotInventory(Block item){
        for (int i = 0; i < 36; i++){
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) {
                if (((BlockItem) mc.player.getInventory().getStack(i).getItem()).getBlock() == item) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void swapSlot(int slot){
        if (slot < 0 || slot > 8) return;
        mc.player.getInventory().selectedSlot = slot;
    }
}
