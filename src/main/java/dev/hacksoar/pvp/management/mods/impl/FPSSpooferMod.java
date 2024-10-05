package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class FPSSpooferMod extends Mod {

	public FPSSpooferMod() {
		super("FPS Spoofer", "Spoof Minecraft framerate", ModCategory.OTHER);
	}

	@Override
	public void setup() {
		this.addSliderSetting("Multiplication", this, 3, 1, 20, true);
	}
}
