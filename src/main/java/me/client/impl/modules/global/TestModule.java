package me.client.impl.modules.global;

import com.google.common.eventbus.Subscribe;
import me.client.api.events.PacketEvent;
import me.client.api.option.impl.NumberOption;
import me.client.impl.Module;

public final class TestModule extends Module {

    public TestModule() {
        super("TestModule", "description", Category.GLOBAL);
    }

    NumberOption speed = new NumberOption.Builder(42).name("Speed").setBounds(0,4).build(this);
    @Override public void tick() {
        System.out.println("Update " + speed.getValue().intValue());

    }
    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        System.out.println("PacketSend");
    }
}