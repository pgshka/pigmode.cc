package me.client.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.client.api.Globals;
import me.client.impl.Feature;
import net.minecraft.command.CommandSource;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public abstract class Command extends Feature implements Globals {

    private final String[] alias;

    public Command(String name, String desc, String... alias) {
        super(name, desc);
        if (alias == null) {
            this.alias = new String[] {name};
        } else {
            this.alias = Arrays.copyOf(alias, alias.length + 1);
            this.alias[ alias.length ] = name.toLowerCase(Locale.ROOT);
        }
    }

    public String[] getAlias() {
        return alias;
    }

    public abstract void exec(LiteralArgumentBuilder<CommandSource> builder);

    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(final String name, final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    protected static LiteralArgumentBuilder<CommandSource> literal(final String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    private static CompletableFuture<Suggestions> suggest(SuggestionsBuilder suggestionsBuilder, String[] suggestions) {
        for(String s : suggestions) {
            suggestionsBuilder.suggest(s);
        }
        return suggestionsBuilder.buildFuture();
    }

}
