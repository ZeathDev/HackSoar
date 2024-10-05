package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import net.minecraft.entity.Entity;

public class EventDamageEntity extends Event{

	private Entity entity;
	
	public EventDamageEntity(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}
