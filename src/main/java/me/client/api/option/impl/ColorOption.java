package me.client.api.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.client.api.Pair;
import me.client.api.option.Option;
import me.client.impl.Feature;
import me.client.impl.Module;

import java.awt.*;

public class ColorOption extends Option<Color> {

    float[] color;

    public ColorOption(Feature feature, String name, String desc, Color value, Option<Boolean> parent) {
        super(feature, name, desc, value, parent);
        this.color = new float[] {value.getRed() / 255f, value.getGreen() / 255f, value.getBlue() / 255f, value.getAlpha() / 255f};
    }

    @Override public void setValue(Color value) {
        this.value = value;
        this.color = new float[] {value.getRed() / 255f, value.getGreen() / 255f, value.getBlue() / 255f, value.getAlpha() / 255f};
    }

    public void setValue(float[] rgba) {
        this.value = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
        this.color = rgba;
    }

    @Override public void setStringValue(String value) {
        this.value = Color.decode(value);
    }

    @Override public Color getValue() {
        return value;
    }

    public float[] asArray() {
        return color;
    }

    public String asHex() {
        return String.format("%02x%02x%02x%02x", value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha());
    }

    @Override public boolean is(String type) {
        return type.equalsIgnoreCase("text");
    }

    public static final class Builder extends OptionBuilder<ColorOption.Builder, Color, ColorOption> {

        public Builder(Color value) {
            super(value);
        }

        @Override public ColorOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            ColorOption o = new ColorOption(feature, name, description, value, parent);
            getOptions().add(o);
            return o;
        }
    }
}
