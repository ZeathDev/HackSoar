package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.ClientUtils;

public class HitDelayFixMod extends Mod {

	public HitDelayFixMod() {
		super("Hit Delay Fix", "Fix delay which is triggered every time you click without attacking a player mob", ModCategory.PLAYER);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		ClientUtils.showNotification("Warning", "This mod is banned on some servers");
	}
}
