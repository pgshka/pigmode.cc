package me.client.api.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;

public record CommandContainer(ArgumentBuilder<CommandSource, ?> builder) {

    public CommandContainer next(CommandContainer container) {
        builder.then(container.builder());
        return this;
    }
}
