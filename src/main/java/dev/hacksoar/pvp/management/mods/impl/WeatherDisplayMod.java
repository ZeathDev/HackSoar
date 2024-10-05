package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.management.mods.HudMod;

public class WeatherDisplayMod extends HudMod {

	public WeatherDisplayMod() {
		super("Weather Display", "Display current weather");
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		super.onRender2D();
	}
	
	@EventTarget
	public void onRenderShadow(EventRenderShadow event) {
		super.onRenderShadow();
	}
	
	@Override
	public String getText() {
		
		String weather = "Clear";
		
		if(mc.theWorld.isRaining()) {
			weather = "Raining";
		}
		
		if(mc.theWorld.isThundering()) {
			weather = "Thundering";
		}
		
		return "Weather: " + weather;
	}
}
