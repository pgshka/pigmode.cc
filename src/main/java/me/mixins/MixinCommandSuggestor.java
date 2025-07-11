package me.mixins;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import me.client.api.Globals;
import me.client.api.manager.CommandManager;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;;

import java.util.concurrent.CompletableFuture;

@Mixin( ChatInputSuggestor.class )
public abstract class MixinCommandSuggestor implements Globals {

    @Shadow private ParseResults<CommandSource> parse;
    @Shadow @Final TextFieldWidget textField;
    @Shadow boolean completingSuggestions;
    @Shadow private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow private ChatInputSuggestor.SuggestionWindow window;

    @Shadow public abstract void show(boolean narrateFirstSuggestion);

    @Inject(method = "refresh",
            at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void refresh(CallbackInfo ci, String string, StringReader reader) {
        String prefix = CommandManager.getPrefix();
        if(reader.canRead(prefix.length()) && reader.getString().startsWith(prefix, reader.getCursor())) {
            reader.setCursor(reader.getCursor() + prefix.length());

            CommandDispatcher<CommandSource> commandDispatcher = CommandManager.DISPATCHER;
            if (this.parse == null) {
                this.parse = commandDispatcher.parse(reader, CommandManager.COMMAND_SOURCE);
            }

            int cursor = textField.getCursor();
            if (cursor >= 1 && (this.window == null || !this.completingSuggestions)) {
                this.pendingSuggestions = commandDispatcher.getCompletionSuggestions(this.parse, cursor);
                this.pendingSuggestions.thenRun(() -> {
                    if (this.pendingSuggestions.isDone()) {
                        this.show(false);
                    }
                });
            }
            ci.cancel();
        }
    }
}