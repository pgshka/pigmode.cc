package me.client.api.events;

import me.client.api.Event;
import net.minecraft.client.sound.SoundInstance;

public class PlaySoundEvent extends Event {
    public SoundInstance sound;

    public PlaySoundEvent(SoundInstance sound) {
        this.sound = sound;
    }

    public SoundInstance getSound() {
        return sound;
    }

}