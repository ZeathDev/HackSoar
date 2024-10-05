package dev.hacksoar.modules.impl.movement.speed.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventUpdate;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.FloatValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.impl.movement.speed.SpeedMode;
import dev.hacksoar.utils.player.MoveUtil;

public class VanillaMode extends SpeedMode {
    private final ListValue mode = new ListValue("Type", new String[]{"Hop", "Ground", "Blink"}, "Hop");
    private final BoolValue fastStop = new BoolValue("Fast Stop", true);
    private final BoolValue fastMode = new BoolValue("Fast Speed", false, () -> !mode.isMode("Blink"));
    private final FloatValue fastModeStrength = new FloatValue("Fast Speed Strength", 1f, 1f, 10f, () -> fastMode.isDisplayable() && fastMode.get());

    public VanillaMode() {
        super("Vanilla");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (MoveUtil.isMoving()) {
            switch (mode.get()) {
                case "Hop":
                    if (mc.thePlayer.onGround) mc.thePlayer.motionY = 0.21;

                    if (!fastMode.get()) {
                        MoveUtil.strafe(0.765);
                    } else {
                        MoveUtil.strafe(fastModeStrength.get());
                    }
                    break;
                case "Ground":
                    if (!fastMode.get()) {
                        MoveUtil.strafe(0.635);
                    } else {
                        MoveUtil.strafe(fastModeStrength.get());
                    }
                    break;
                case "Blink":
                    // TODO Unfinished
                    break;
            }
        } else {
            if (fastStop.get())
                MoveUtil.stop(false);
        }
    }
}
