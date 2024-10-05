package dev.hacksoar.manages;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventLoadWorld;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

public class BotManager extends ArrayList<Entity> {

    public void init() {
        HackSoar.instance.eventManager.register(this);
    }

    @EventTarget
    public void onLoadWorld(EventLoadWorld loadWorld) {
        this.clear();
    }

    public boolean add(Entity entity) {
        if (!this.contains(entity)) super.add(entity);
        return false;
    }
}
