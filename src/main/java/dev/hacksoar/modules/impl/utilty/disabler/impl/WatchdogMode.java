package dev.hacksoar.modules.impl.utilty.disabler.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventPreUpdate;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.modules.impl.utilty.disabler.DisablerMode;
import dev.hacksoar.utils.player.PlayerUtils;

@ModuleTag
public class WatchdogMode extends DisablerMode {
    public WatchdogMode() {
        super("Watchdog");
    }

    public BoolValue disable = new BoolValue("disable",true);

    @EventTarget
    public void onPreUpdate(EventPreUpdate event) {
        PlayerUtils.tellPlayer("Watchdog was " + (disable.get() ? "disable" : "enable"));
    }
}
