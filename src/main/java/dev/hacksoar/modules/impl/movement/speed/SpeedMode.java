package dev.hacksoar.modules.impl.movement.speed;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.value.Value;
import dev.hacksoar.modules.impl.movement.Speed;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liycxc
 */
public class SpeedMode {
    public String modeName;
    public Speed speed = (Speed) HackSoar.instance.moduleManager.getModule("Speed");
    public Minecraft mc = Minecraft.getMinecraft();

    public SpeedMode(String modeName) {
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
