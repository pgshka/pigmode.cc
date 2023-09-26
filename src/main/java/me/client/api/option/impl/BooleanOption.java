package me.client.api.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.client.api.Pair;
import me.client.api.option.Option;
import me.client.impl.Feature;
import me.client.impl.Module;

public class BooleanOption extends Option<Boolean> {

    private BooleanOption(Feature feature, String name, String desc, Boolean value, Option<Boolean> parent) {
        super(feature, name, desc, value, parent);
    }

    @Override public void setValue(Boolean value) {
        this.value = value;
    }

    @Override public void setStringValue(String value) {
        this.value = Boolean.parseBoolean(value);
    }

    @Override public Boolean getValue() {
        return value;
    }

    @Override public boolean is(String type) {
        return type.equalsIgnoreCase("bool");
    }

    public static final class Builder extends OptionBuilder<Builder, Boolean, BooleanOption> {

        public Builder(Boolean value) {
            super(value);
        }

        @Override public BooleanOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            BooleanOption o = new BooleanOption(feature, name, description, value, parent);
            getOptions().add(o);
            return o;
        }
    }
}
