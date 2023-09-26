package me.client.api.manager;


import me.client.impl.Module;
import me.client.impl.modules.anarchy.BedAura;
import me.client.impl.modules.anarchy.ElytraFly;
import me.client.impl.modules.client.*;
import me.client.impl.modules.global.*;
import me.client.impl.modules.legit.AnchorAura;
import me.client.impl.modules.legit.CrystalBreak;
//import me.client.impl.modules.anarchy.*;
//import me.client.impl.modules.legit.*;
//import me.client.impl.modules.wynncraft.*;

import java.util.ArrayList;
import java.util.Arrays;

public final class ModuleManager extends ArrayList<Module> implements Manager<ModuleManager>{

    @Override public ModuleManager load() {
        addAll(
                Arrays.asList(
                    new TestModule(),
                        new ClickGui(),
                        new FastXP(),
                        new CrystalBreak(),
                        new ElytraFly(),
                        new NoRender(),
                        new AnchorAura(),
                        new MiddleClick(),
                        new Tracers(),
                        new BoxESP(),
                        new FakePlayer(),
                        new AntiAim(),
                        new BedAura(),
                        new Hud()
                )
        );
        return this;
    }

    @Override public ModuleManager unload() {
        forEach(m -> {
            if (m.isToggled()) m.disable();
        });
        clear();
        return this;
    }

    public Module get(String name) {
        return stream().filter(m -> m.getLabel().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked") public <T extends Module> T get(Class<T> module) {
        return (T) stream().filter(m -> m.getClass() == module).findAny().orElse(null);
    }

}