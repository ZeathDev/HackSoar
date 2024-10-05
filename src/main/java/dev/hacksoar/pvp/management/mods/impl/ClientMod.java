package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class ClientMod extends Mod {

	public ClientMod() {
		super("Client", "For client settings", ModCategory.OTHER);
	}
	
	@Override
	public void setup() {
		this.addBooleanSetting("DarkMode", this, false);

		this.addSliderSetting("Volume", this, 1.0, 0, 1.0, false);
		this.addBooleanSetting("Random", this, false);
		this.addBooleanSetting("Loop", this, false);
		
		this.setHide(true);
	}
}
