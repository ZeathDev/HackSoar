package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;

public class EventScrollMouse extends Event{

	private int amount;
	
	public EventScrollMouse(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
}
