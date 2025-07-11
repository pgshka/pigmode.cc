package me.client.api.events;


import me.client.api.Event;
import net.minecraft.network.packet.Packet;

public class PacketEvent extends Event {
    private final Packet<?> packet;
    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public static final class Receive extends PacketEvent {
        public Receive(Packet<?> packet) { super(packet); }
    }

    public static final class Send extends PacketEvent {
        public Send(Packet<?> packet) { super(packet); }
    }

}