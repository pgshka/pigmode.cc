package me.client.impl.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.client.api.utils.ChatUtil;
import me.client.impl.Module;
import me.client.api.command.Command;
import me.client.api.command.args.ModuleArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;

import static me.client.api.command.args.ModuleArgumentType.module;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", "Toggles a module");
    }

    @Override public void exec(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                argument("module", module())
                        .executes(context -> {
                            Module module = ModuleArgumentType.getModule(context, "module");

                            if (module.isToggled()){
                                ChatUtil.send(Formatting.YELLOW + module.getLabel() + Formatting.RESET + " Disabled");
                            } else ChatUtil.send(Formatting.YELLOW + module.getLabel() + Formatting.RESET + " Enabled");

                            module.toggle();
                            return 1;
                        })
        ).executes(context -> 0);
    }

}