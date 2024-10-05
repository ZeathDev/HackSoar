package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;

public class EventRenderSelectedItem extends Event{

	private int color;
	
	public EventRenderSelectedItem(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
}
