package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class InventoryMod extends Mod {

	public InventoryMod() {
		super("Inventory", "Customize Inventory", ModCategory.RENDER);
	}

	@Override
	public void setup() {
		this.addBooleanSetting("Animation", this, false);
		this.addBooleanSetting("Click out of Container", this, false);
		this.addSliderSetting("Speed", this, 18, 5, 20, true);
		this.addBooleanSetting("Prevent Potion Shift", this, true);
		this.addBooleanSetting("Transparent background", this, false);
	}
}
