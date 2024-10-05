package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class SlowSwingMod extends Mod {

	public SlowSwingMod() {
		super("Slow Swing", "Slow down swing speed", ModCategory.OTHER);
	}
	
	@Override
	public void setup() {
		this.addSliderSetting("Delay", this, 14, 2, 20, true);
	}

}
