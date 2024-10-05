package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.management.mods.HudMod;
import dev.hacksoar.utils.server.ServerUtils;

public class PingDisplayMod extends HudMod {

	public PingDisplayMod() {
		super("Ping Display", "Display your ping");
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		super.onRender2D();
	}
	
	@EventTarget
	public void onRenderShdow(EventRenderShadow event) {
		super.onRenderShadow();
	}
	
	@Override
	public String getText() {
		return ServerUtils.getPing() + " ms";
	}
}
