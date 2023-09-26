package me.mixins;

import me.client.api.Globals;
import me.client.PigMode;
import me.client.api.utils.ChatUtil;
import me.client.impl.Module;
import me.client.impl.commands.BindCommand;
import net.minecraft.client.Keyboard;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin( Keyboard.class )
public class MixinKeyboard implements Globals {

    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "net/minecraft/client/util/InputUtil.isKeyPressed(JI)Z", ordinal = 5), cancellable = true)
    private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (BindCommand.module != null) {
            BindCommand.module.setKey(key);

            ChatUtil.send("Changed bind for " + Formatting.YELLOW + BindCommand.module.getLabel() + Formatting.RESET + " to " + Formatting.YELLOW + InputUtil.fromKeyCode(key, scanCode).getTranslationKey());

            BindCommand.module = null;
            ci.cancel();
        } else {
            PigMode.getDefault().getModuleManager().stream().filter(module -> module.getKey() == key).forEach(Module::toggle);
        }
    }
}