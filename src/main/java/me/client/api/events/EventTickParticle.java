package me.client.api.events;

import me.client.api.Event;
import net.minecraft.client.particle.Particle;

public class EventTickParticle extends Event {

    private final Particle particle;

    public EventTickParticle(Particle particle) {
        this.particle = particle;
    }

    public Particle getParticle() {
        return particle;
    }

    public enum Type {
        FIREWORK, EXPLOSION, SMOKE
    }
}