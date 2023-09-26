package me.mixins;

import io.netty.channel.Channel;
import me.client.PigMode;
import me.client.api.events.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ClientConnection.class )
public class MixinClientConnection {

    @Shadow private Channel channel;
    @Shadow @Final private NetworkSide side;

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void coffee_handlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        PacketEvent.Receive pe = new PacketEvent.Receive(packet);
        PigMode.EVENT_BUS.post(pe);
        if (pe.isCancelled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "send(Lnet/minecraft/network/packet/Packet;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V"))
    void replacePacket(ClientConnection instance, Packet<?> packet, PacketCallbacks callbacks) {
        PacketEvent.Send pe = new PacketEvent.Send(packet);
        PigMode.EVENT_BUS.post(pe);
        if (!pe.isCancelled()) {
            instance.send(pe.getPacket(), callbacks);
        }
    }
}