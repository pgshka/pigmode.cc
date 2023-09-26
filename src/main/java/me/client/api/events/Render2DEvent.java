/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package me.client.api.events;

import net.minecraft.client.gui.DrawContext;

public class Render2DEvent {
    public DrawContext drawContext;
    public int screenWidth, screenHeight;
    public double frameTime;
    public float tickDelta;

    public Render2DEvent(DrawContext drawContext, int screenWidth, int screenHeight, float tickDelta) {
        this.drawContext = drawContext;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.tickDelta = tickDelta;
    }
}