package me.client.impl.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.client.PigMode;
import me.client.api.command.args.OptionArgumentType;
import me.client.api.manager.CommandManager;
import me.client.api.utils.ChatUtil;
import me.client.impl.Module;
import me.client.api.command.Command;
import me.client.api.command.args.ModuleArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import static me.client.api.command.args.ModuleArgumentType.module;

public class WatermarkCommand extends Command {

    public WatermarkCommand() {
        super("watermark", "Change a watermark");
    }

    @Override
    public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ChatUtil.send("How to change watermark? Example: " + Formatting.YELLOW + CommandManager.getPrefix() + "watermark \"^\"");
            return 1;
        }).then(
                argument("watermark", StringArgumentType.string())
                        .executes(context -> {
                            String old = PigMode.getDefault().getWatermark();
                            String prefix = StringArgumentType.getString(context, "watermark");
                            PigMode.getDefault().setWatermark(prefix);
                            ChatUtil.send("Watermark changed from " + Formatting.YELLOW + old + Formatting.WHITE + " to " + Formatting.YELLOW + prefix);
                            return 1;
                        })
        );
    }
}