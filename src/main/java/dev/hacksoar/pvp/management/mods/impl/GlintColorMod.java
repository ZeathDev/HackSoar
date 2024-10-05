package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.pvp.management.settings.Setting;
import dev.hacksoar.utils.color.ColorUtils;

import java.awt.*;

public class GlintColorMod extends Mod {

	public static GlintColorMod instance = new GlintColorMod();
	
	public GlintColorMod() {
		super("Glint Color", "Customize Glint Color", ModCategory.RENDER);
	}

	@Override
	public void setup() {
		HackSoar.instance.settingsManager.addSetting(new Setting("Rainbow", this, false));
	}
	
	public Color getColor() {
		if(HackSoar.instance.settingsManager.getSettingByClass(GlintColorMod.class, "Rainbow").getValBoolean()) {
			return ColorUtils.rainbow(0, 25, 255);
		}
		
		return ColorUtils.getClientColor(0);
	}
}
