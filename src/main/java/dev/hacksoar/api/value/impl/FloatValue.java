package dev.hacksoar.api.value.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.hacksoar.api.value.Function0;
import dev.hacksoar.api.value.Value;
import lombok.Getter;

@Getter
public class FloatValue extends Value<Float> {
    private final Float minimum;
    private final Float maximum;
    private final String suffix;

    public FloatValue(String name, Float value, Float minimum, Float maximum, String suffix,
                      Function0<Boolean> displayable) {
        super(name, value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.suffix = suffix;
    }

    public FloatValue(String name, Float value, Float minimum, Float maximum,
                      Function0<Boolean> displayable) {
        this(name, value, minimum, maximum, "", displayable);
    }

    public FloatValue(String name, Float value, Float minimum, Float maximum, String suffix) {
        this(name, value, minimum, maximum, suffix, () -> true);
    }

    public FloatValue(String name, Float value, Float minimum, Float maximum) {
        this(name, value, minimum, maximum, "", () -> true);
    }

    public void set(Number newValue) {
        set(newValue.floatValue());
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            setValue(element.getAsFloat());
        }
    }
}
