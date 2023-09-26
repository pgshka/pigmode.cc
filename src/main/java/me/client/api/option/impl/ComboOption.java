package me.client.api.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.client.api.Pair;
import me.client.api.option.Option;
import me.client.impl.Feature;
import me.client.impl.Module;

import java.util.List;

public class ComboOption extends Option<String> {

    private final List<String> combo;

    public ComboOption(Feature feature, String name, String desc, String value, Option<Boolean> parent, List<String> combo) {
        super(feature, name, desc, value, parent);
        this.combo = combo;
    }

    public List<String> getCombo() {
        return combo;
    }

    @Override public void setValue(String value) {
        if(!combo.contains(value)) return;
        this.value = value;
    }

    @Override public void setStringValue(String value) {
        setValue(value);
    }

    @Override public String getValue() {
        return value;
    }

    @Override public boolean is(String type) {
        return type.equalsIgnoreCase("combo");
    }

    public void increase() {
        if(combo.indexOf(value) + 1 >= combo.size()) setValue(combo.get(0));
        else setValue(combo.get(combo.indexOf(value) + 1));
    }

    public static final class Builder extends OptionBuilder<ComboOption.Builder, String, ComboOption> {

        private List<String> combo;

        public Builder(String value) {
            super(value);
        }

        public Builder setCombo(List<String> combo) {
            this.combo = combo;
            return this;
        }

        public Builder setCombo(String... combo) {
            this.combo = List.of(combo);
            return this;
        }

        @Override public ComboOption build(Feature feature) {
            if(validate()) throw new NullPointerException();
            ComboOption o = new ComboOption(feature, name, description, value, parent, combo);
            getOptions().add(o);
            return o;
        }

    }
}
