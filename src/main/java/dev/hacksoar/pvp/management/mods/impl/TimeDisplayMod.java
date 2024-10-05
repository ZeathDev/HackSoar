package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.management.mods.HudMod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeDisplayMod extends HudMod {

	public TimeDisplayMod() {
		super("Time Display", "Display current time");
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
		
        Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("HH:mm a", Locale.US);
        String time = df.format(c.getTime());
        
        return time;
	}
}
