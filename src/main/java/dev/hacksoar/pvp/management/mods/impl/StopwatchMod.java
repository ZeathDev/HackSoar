package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventKey;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.api.events.impl.EventTick;
import dev.hacksoar.pvp.management.mods.HudMod;
import dev.hacksoar.utils.timer.TimerUtils;

import java.text.DecimalFormat;

public class StopwatchMod extends HudMod {

	private final TimerUtils timer = new TimerUtils();
	private int pressCount;
	private float currentTime;
	private final DecimalFormat timeFormat = new DecimalFormat("0.00");
	
	public StopwatchMod() {
		super("Stopwatch", "Measure the time");
	}

	@Override
	public void onEnable() {
		super.onEnable();
		if(timer != null) {
			timer.reset();
		}
		pressCount = 0;
		currentTime = 0;
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		switch(pressCount) {
			case 0:
				timer.reset();
				break;
			case 1:
				currentTime = (timer.getElapsedTime() / 1000F);
				break;
			case 3:
				timer.reset();
				currentTime = 0;
				pressCount = 0;
				break;
		}
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		if(HackSoar.instance.keyBindManager.STOPWATCH.isKeyDown()) {
			pressCount++;
		}
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
		return timeFormat.format(currentTime) + " s";
	}
}
