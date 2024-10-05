package dev.hacksoar.modules.impl.movement.noslow.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventSlowDown;
import dev.hacksoar.modules.impl.movement.noslow.NoSlowMode;

public class VanillaMode extends NoSlowMode {
    public VanillaMode() {
        super("Vanilla");
    }

    @EventTarget
    public void onSlowDown(EventSlowDown event) {
        event.setCancelled(true);
    }
}
