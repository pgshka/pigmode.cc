package me.client.api.utils;

import me.client.PigMode;
import me.client.api.Globals;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatUtil implements Globals {

    private ChatUtil() {
        throw new IllegalArgumentException();
    }

    public static void send(String message) {
        if(mc.inGameHud == null) return;
        mc.inGameHud.getChatHud().addMessage(getDefaultPrefix().append(" ").append(Text.of(message)));
    }

    public static MutableText getDefaultPrefix() {
        return Text.literal(
                        String.format("%s{%s}%s", Formatting.LIGHT_PURPLE, PigMode.WATERMARK, Formatting.RESET)
                );
    }

}