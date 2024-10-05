package dev.hacksoar.api.value.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.hacksoar.api.value.Function0;
import dev.hacksoar.api.value.Value;

public class BoolValue extends Value<Boolean> {

    public BoolValue(String name, Boolean value,
                     Function0<Boolean> displayable) {
        super(name, value, displayable);
    }

    public BoolValue(String name, Boolean value) {
        this(name, value, () -> true);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            if (element.getAsBoolean() || element.getAsString().equalsIgnoreCase("true")) {
                setValue(true);
            } else {
                setValue(false);
            }
        }
    }
}