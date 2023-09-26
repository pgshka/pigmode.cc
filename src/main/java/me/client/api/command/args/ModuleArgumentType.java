package me.client.api.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.client.PigMode;
import me.client.api.Globals;
import me.client.impl.Module;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class ModuleArgumentType implements ArgumentType<Module>, Globals {

    public static Module getModule(CommandContext<?> context, String name) {
        return context.getArgument(name, Module.class);
    }

    public static ModuleArgumentType module() {
        return new ModuleArgumentType();
    }

    @Override public Module parse(StringReader reader) throws CommandSyntaxException {
        Module module = PigMode.getDefault().getModuleManager().get(reader.readString());
        if(module == null) throw  new DynamicCommandExceptionType(o ->
                Text.of(o + " doesn't exist")).create(reader.readString());
        return module;
    }

    @Override public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        pigmode().getModuleManager().forEach(m -> builder.suggest(m.getLabel()));
        return builder.buildFuture();
    }

}
