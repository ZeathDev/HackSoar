package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.pvp.management.settings.Setting;

public class MenuBlurMod extends Mod {

	public MenuBlurMod() {
		super("Menu Blur", "Blur the menu", ModCategory.RENDER);
	}

	@Override
	public void setup() {
		HackSoar.instance.settingsManager.addSetting(new Setting("Radius", this, 20, 1, 40, true));
	}
}
