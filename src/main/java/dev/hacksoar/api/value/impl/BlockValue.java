package dev.hacksoar.api.value.impl;

import dev.hacksoar.api.value.Function0;

public class BlockValue extends IntValue {

    public BlockValue(String name, Integer value, Function0<Boolean> displayable) {
        super(name, value, 1, 197, displayable);
    }

    public BlockValue(String name, Integer value) {
        this(name, value, () -> true);
    }
}
