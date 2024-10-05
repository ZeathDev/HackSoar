package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;

public class EventPlayerHeadRotation extends Event{

	private float yaw;
	private float pitch;
	
	public EventPlayerHeadRotation(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}
}