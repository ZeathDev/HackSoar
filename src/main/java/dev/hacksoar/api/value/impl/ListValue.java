package dev.hacksoar.api.value.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.hacksoar.api.value.Function0;
import dev.hacksoar.api.value.Value;
import lombok.Getter;

import java.util.Arrays;

public class ListValue extends Value<String> {
    @Getter
    private String[] values;
    public boolean openList = false;

    public ListValue(String name, String[] values, String value,
                     Function0<Boolean> displayable) {
        super(name, value, displayable);
        this.values = values;
        this.setValue(value);
    }

    public ListValue(String name, String[] values, String value) {
        this(name, values, value, () -> true);
    }

    public boolean containsValue(String string) {
        return Arrays.stream(values).anyMatch(it -> it.equalsIgnoreCase(string));
    }

    @Override
    public void changeValue(String value) {
        for (String element : values) {
            if (element.equalsIgnoreCase(value)) {
                this.setValue(element);
                break;
            }
        }
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            changeValue(element.getAsString());
        }
    }

    public boolean isMode(String string) {
        return getValue().equalsIgnoreCase(string);
    }

    public int indexOf(String mode) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equalsIgnoreCase(mode)) {
                return i;
            }
        }
        return 0;
    }

    public boolean ListContains(String string) {
        return Arrays.stream(values).anyMatch(s -> s.equalsIgnoreCase(string));
    }

    public int getModeListNumber(String mode) {
        return Arrays.asList(values).indexOf(mode);
    }
}
