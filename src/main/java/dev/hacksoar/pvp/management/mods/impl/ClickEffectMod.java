package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class ClickEffectMod extends Mod {

	public ClickEffectMod() {
		super("Click Effect", "Show effects when clicked", ModCategory.OTHER);
	}

	@Override
	public void setup() {
		this.addBooleanSetting("Accent Color", this, true);
	}
}
