package me.client.api.option.impl;

import me.client.api.option.Option;
import me.client.impl.Feature;

public class SeparatorOption extends Option<String> {

    public SeparatorOption(Feature feature, String name, String desc, String value, Option<Boolean> parent) {
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

    public static final class Builder extends OptionBuilder<SeparatorOption.Builder, String, SeparatorOption> {

        public Builder(String value) {
            super(value);
        }

        @Override public SeparatorOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            SeparatorOption o = new SeparatorOption(feature, name, description, value, parent);
            getOptions().add(o);
            return o;
        }

    }
}
