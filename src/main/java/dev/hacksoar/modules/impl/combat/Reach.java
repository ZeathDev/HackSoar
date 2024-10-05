package dev.hacksoar.modules.impl.combat;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventMouseOver;
import dev.hacksoar.api.events.impl.EventPreMotion;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.FloatValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import org.lwjgl.input.Mouse;

@ModuleTag
public class Reach extends Module {
    public Reach() {
        super("Reach","Like long arm monkeys", ModuleCategory.Combat);
    }
    public static FloatValue maxValue = new FloatValue("Reach value",4f,3f,5f);

    private int exempt = 0;

    @EventTarget
    public void onPreMotion (EventPreMotion eventMotion) {
        exempt--;
    }
    @EventTarget
    public void onMouseOver(EventMouseOver eventMouseOver) {
        if (Mouse.isButtonDown(1)) {
            exempt = 1;
        }

        if (exempt > 0) return;

        eventMouseOver.setRange(maxValue.get());
    }
}
