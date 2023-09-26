package me.client.impl.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.client.api.utils.ChatUtil;
import me.client.impl.Module;
import me.client.api.command.Command;
import me.client.api.command.args.ModuleArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "Manages binds");
    }

    public static Module module;

    @Override
    public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                argument("module", ModuleArgumentType.module())
                        .then(
                                literal("set")
                                        .executes(context -> {
                                            module = ModuleArgumentType.getModule(context, "module");
                                            ChatUtil.send("Press a key");
                                            return 1;
                                        })
                        ).then(
                                literal("clear")
                                        .executes(context -> {
                                            Module m = ModuleArgumentType.getModule(context, "module");
                                            m.setKey(-1481058891);
                                            ChatUtil.send("Cleared bind for " + Formatting.YELLOW + m.getLabel());
                                            return 1;
                                        })
                        )
        );
    }
}