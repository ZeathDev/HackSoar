package dev.hacksoar.modules.impl.movement;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventLoadWorld;
import dev.hacksoar.api.events.impl.EventUpdate;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.FloatValue;
import dev.hacksoar.api.value.impl.IntValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;

@ModuleTag
public class Boost extends Module {
    private final FloatValue timer = new FloatValue("Timer Multiplier", 2f, 1f, 10f);
    private final IntValue ticks = new IntValue("Ticks", 10, 1, 100);

    private int passedTicks = 0;

    public Boost() {
        super("Boost", "fast move", ModuleCategory.Movement);
    }

    @Override
    public void onDisable() {
        passedTicks = 0;
        mc.timer.timerSpeed = 1;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        passedTicks = mc.thePlayer.ticksExisted;
    }

    @EventTarget
    public void onWorld(EventLoadWorld event) {
        toggle();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.timer.timerSpeed = timer.get();
        if (mc.thePlayer.ticksExisted - ticks.get() >= passedTicks) {
            mc.timer.timerSpeed = 1;
            toggle();
        }
    }
}
