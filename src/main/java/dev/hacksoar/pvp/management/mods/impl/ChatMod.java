package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class ChatMod extends Mod {

	public ChatMod() {
		super("Chat", "Customize chat", ModCategory.RENDER);
	}

	@Override
	public void setup() {
		this.addBooleanSetting("Bar Animation", this, false);
		this.addBooleanSetting("Smooth", this, true);
		this.addSliderSetting("Smooth Speed", this, 4, 1, 10, true);
		this.addBooleanSetting("Transparent background", this, true);
	}
}
