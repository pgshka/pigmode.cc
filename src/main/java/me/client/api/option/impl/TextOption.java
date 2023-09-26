package me.client.api.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.client.api.Pair;
import me.client.api.option.Option;
import me.client.impl.Feature;
import me.client.impl.Module;

public class TextOption extends Option<String> {

    public TextOption(Feature feature, String name, String desc, String value, Option<Boolean> parent) {
        super(feature, name, desc, value, parent);
    }

    @Override public void setValue(String value) {
        this.value = value;
    }

    @Override public void setStringValue(String value) {
        this.value = value;
    }

    @Override public String getValue() {
        return value;
    }

    @Override public boolean is(String type) {
        return type.equalsIgnoreCase("text");
    }

    public static final class Builder extends OptionBuilder<TextOption.Builder, String, TextOption> {

        public Builder(String value) {
            super(value);
        }

        @Override public TextOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            TextOption o = new TextOption(feature, name, description, value, parent);
            getOptions().add(o);
            return o;
        }

    }
}
