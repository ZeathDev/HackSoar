package dev.hacksoar.api.value.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.hacksoar.api.value.Function0;
import dev.hacksoar.api.value.Value;
import lombok.Getter;

@Getter
public class IntValue extends Value<Integer> {

    private final Integer minimum;
    private final Integer maximum;
    private final String suffix;

    public IntValue(String name, Integer value, Integer minimum, Integer maximum, String suffix,
                    Function0<Boolean> displayable) {
        super(name, value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.suffix = suffix;
    }

    public IntValue(String name, Integer value, Integer minimum, Integer maximum,
                    Function0<Boolean> displayable) {
        this(name, value, minimum, maximum, "", displayable);
    }

    public IntValue(String name, Integer value, Integer minimum, Integer maximum, String suffix) {
        this(name, value, minimum, maximum, suffix, () -> true);
    }

    public IntValue(String name, Integer value, Integer minimum, Integer maximum) {
        this(name, value, minimum, maximum, "", () -> true);
    }

    public void set(Number newValue) {
        set(newValue.intValue());
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            setValue(element.getAsInt());
        }
    }
}
