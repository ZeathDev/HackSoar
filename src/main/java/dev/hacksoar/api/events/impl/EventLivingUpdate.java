package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import net.minecraft.entity.EntityLivingBase;

public class EventLivingUpdate extends Event{
	
	private EntityLivingBase entity;
	
	public EventLivingUpdate(EntityLivingBase entity) {
		this.entity = entity;
	}

	public EntityLivingBase getEntity() {
		return entity;
	}
}