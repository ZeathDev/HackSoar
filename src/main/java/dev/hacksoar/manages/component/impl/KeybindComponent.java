package dev.hacksoar.manages.component.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventKey;
import dev.hacksoar.manages.component.Component;
import dev.hacksoar.modules.Module;

import java.util.Map;

public class KeybindComponent extends Component {
    @EventTarget
    public void onKey(EventKey key) {
        if (mc.currentScreen != null) {
            return;
        }
        if (HackSoar.instance.moduleManager.keyBinds.containsKey(key.getKey())) {
            for (Map.Entry<Integer, Module> entry : HackSoar.instance.moduleManager.keyBinds.entries()) {
                if (entry.getKey().equals(key.getKey())) {
                    entry.getValue().toggle();
                }
            }
        }
    }
}
