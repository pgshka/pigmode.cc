    package me.client.api.option.impl;

    import com.google.gson.JsonElement;
    import com.google.gson.JsonObject;
    import com.google.gson.JsonPrimitive;
    import me.client.api.Pair;
    import me.client.api.option.Option;
    import me.client.impl.Feature;
    import me.client.impl.Module;

    public class NumberOption extends Option<Number> {

        private transient final float max, min;

        private NumberOption(Feature feature, String name, String desc, Number value, float max, float min, Option<Boolean> parent) {
            super(feature, name, desc, value, parent);
            this.max = max;
            this.min = min;
        }

        @Override public void setValue(Number value) {
            this.value = Math.max(getMin(), Math.min((float) value, getMax()));
        }

        @Override public void setStringValue(String value) {
            try {
                setValue(Float.parseFloat(value));
            } catch (Exception ignored) {}
        }

        @Override public Number getValue() {
            return value;
        }

        @Override public boolean is(String type) {
            return type.equalsIgnoreCase("number");
        }

        public boolean withPoint() {
            return value instanceof Double || value instanceof Float;
        }

        public float getMax() {
            return max;
        }

        public float getMin() {
            return min;
        }

        public static final class Builder extends OptionBuilder<Builder, Number, NumberOption> {

            private float max, min;

            public Builder(Number value) {
                super(value);
            }

            public Builder setBounds(float min, float max) {
                this.max = max;
                this.min = min;
                return this;
            }

            @Override public NumberOption build(Feature feature) {
                if(validate()) throw new NullPointerException();
                NumberOption o = new NumberOption(feature, name, description, value, max, min, parent);
                getOptions().add(o);
                return o;
            }

        }
    }
