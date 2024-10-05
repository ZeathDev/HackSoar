package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventKey;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class HypixelQuickPlayMod extends Mod {

	public HypixelQuickPlayMod() {
		super("Hypixel Quick Play", "Quickly enter Hypixel commands", ModCategory.PLAYER);
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		if(event.getKey() == HackSoar.instance.keyBindManager.QUICKPLAY.getKeyCode()) {
	    	mc.displayGuiScreen(HackSoar.instance.guiManager.getGuiQuickPlay());
		}
	}
}
