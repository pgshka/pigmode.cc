package me.client.api.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.client.api.Globals;
import me.client.api.option.Option;
import me.client.impl.Feature;
import me.client.impl.Module;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

//TODO add script support
public class OptionArgumentType implements ArgumentType<String>, Globals {

    public static OptionArgumentType option() {
        return new OptionArgumentType();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Stream<String> stream = Option.getForTarget(context.getArgument("module", Module.class)).stream().map(Feature::getLabel);

        return CommandSource.suggestMatching(stream, builder);
    }

    public static Option<?> getOption(CommandContext<?> context, String moduleArg, String optionArg) throws CommandSyntaxException {
        Module module = context.getArgument(moduleArg, Module.class);
        String optionLabel = context.getArgument(optionArg, String.class);

        Optional<Option<?>> option = Option.getForTarget(module).stream().filter(o -> o.getLabel().equalsIgnoreCase(optionLabel)).findFirst();
        if (option.isEmpty())
            throw new DynamicCommandExceptionType(o -> Text.of("Option Not Found " + o)).create(optionLabel);

        return option.get();
    }

}