package me.client.impl.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.client.api.command.args.OptionArgumentType;
import me.client.api.manager.CommandManager;
import me.client.api.utils.ChatUtil;
import me.client.impl.Module;
import me.client.api.command.Command;
import me.client.api.command.args.ModuleArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import static me.client.api.command.args.ModuleArgumentType.module;

public class PrefixCommand extends Command {

    public PrefixCommand() {
        super("prefix", "Change a prefix");
    }

    @Override
    public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ChatUtil.send("How to change prefix? Example: " + Formatting.YELLOW + CommandManager.getPrefix() + "prefix \"^\"");
            return 1;
        }).then(
                argument("prefix", StringArgumentType.string())
                        .executes(context -> {
                            String old = CommandManager.getPrefix();
                            String prefix = StringArgumentType.getString(context, "prefix");
                            CommandManager.setPrefix(prefix);
                            ChatUtil.send("Command prefix changed from " + Formatting.YELLOW + old + Formatting.RESET + " to " + Formatting.YELLOW + prefix);
                            return 1;
                        })
        );
    }
}