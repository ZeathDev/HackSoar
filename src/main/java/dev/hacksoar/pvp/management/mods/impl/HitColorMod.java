package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventHitOverlay;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.color.ColorUtils;

import java.awt.*;

public class HitColorMod extends Mod {

	public HitColorMod() {
		super("HitColor", "Change the color of the hit", ModCategory.RENDER);
	}

	@Override
	public void setup() {
		this.addSliderSetting("Alpha", this, 0.8, 0, 1, false);
	}
	
	@EventTarget
	public void onHitOverlay(EventHitOverlay event) {
		Color color = ColorUtils.getClientColor(0);
		float alpha = (float) HackSoar.instance.settingsManager.getSettingByName(this, "Alpha").getValDouble();
		
		event.setRed(color.getRed() / 255F);
		event.setGreen(color.getGreen() / 255F);
		event.setBlue(color.getBlue() / 255F);
		event.setAlpha(alpha);
	}
}
