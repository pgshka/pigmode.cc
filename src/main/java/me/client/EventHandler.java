package me.client;

import com.google.common.eventbus.Subscribe;
import me.client.api.Globals;
import me.client.api.events.PacketEvent;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public final class EventHandler implements Globals {

    public static final EventHandler INSTANCE = new EventHandler();

    EventHandler() { }
}