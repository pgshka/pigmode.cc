package me.mixins;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.client.PigMode;
import me.client.api.Globals;
import me.client.api.manager.CommandManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessageHook(String content, CallbackInfo ci) {
        if(content.startsWith(CommandManager.getPrefix())) {
            try {
                CommandManager.DISPATCHER.execute(CommandManager.DISPATCHER.parse(content.substring(CommandManager.getPrefix().length()), CommandManager.COMMAND_SOURCE));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            } finally {
                ci.cancel();
            }
        }
    }
}