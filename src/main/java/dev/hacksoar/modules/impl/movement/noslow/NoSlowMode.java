package dev.hacksoar.modules.impl.movement.noslow;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.value.Value;
import dev.hacksoar.modules.impl.movement.NoSlow;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * This file is part of LikeSoar project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-07-10
 * @time: 14:20
 */
public class NoSlowMode {
    public String modeName;
    public NoSlow noSlow = (NoSlow) HackSoar.instance.moduleManager.getModule("NoSlow");
    public Minecraft mc = Minecraft.getMinecraft();

    public NoSlowMode(String modeName) {
        this.modeName = modeName;
    }

    public List<Value<?>> getValues() {
        List<Value<?>> values = new ArrayList<>();
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Value.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    Value<?> value = (Value<?>) field.get(this);
                    values.add(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return values;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onInitialize() {
    }
}
