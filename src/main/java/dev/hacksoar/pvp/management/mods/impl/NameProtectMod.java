package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventText;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class NameProtectMod extends Mod {

	public NameProtectMod() {
		super("Name Protect", "Protect your name", ModCategory.RENDER);
	}

	@EventTarget
	public void onText(EventText event) {
		event.replace(mc.getSession().getUsername(), "You");
	}
}
