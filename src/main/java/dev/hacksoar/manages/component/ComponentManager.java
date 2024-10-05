package dev.hacksoar.manages.component;

import dev.hacksoar.HackSoar;
import dev.hacksoar.manages.component.impl.*;

import java.util.ArrayList;

public final class ComponentManager extends ArrayList<Component> {

    /**
     * Called on client start and when for some reason when we reinitialize
     */
    public void init() {
        this.add(new RotationComponent());
        this.add(new InventoryDeSyncComponent());
        this.add(new BadPacketsComponent());
        this.add(new KeybindComponent());
        this.add(new SlotComponent());
        this.add(new SelectorDetectionComponent());
        this.add(new BlinkComponent());
        this.add(new SmoothCameraComponent());
        this.registerToEventBus();
    }

    public void registerToEventBus() {
        for (Component component : this) {
            HackSoar.instance.eventManager.register(component);
        }
    }
}
