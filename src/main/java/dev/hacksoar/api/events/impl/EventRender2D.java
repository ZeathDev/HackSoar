package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author Liycxc
 */
public class EventRender2D extends Event{
	private ScaledResolution scaledResolution;

	private float partialTicks;
	
	public EventRender2D(float partialTicks,ScaledResolution scaledResolution) {
		this.scaledResolution = scaledResolution;
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

	public void setPartialTicks(float partialTicks) {
		this.partialTicks = partialTicks;
	}
	public ScaledResolution getScaledResolution() {
		return scaledResolution;
	}
}
