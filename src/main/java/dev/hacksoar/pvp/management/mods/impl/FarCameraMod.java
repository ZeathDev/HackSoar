package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventCameraRotation;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import net.minecraft.client.renderer.GlStateManager;

public class FarCameraMod extends Mod {

	public FarCameraMod() {
		super("Far Camera", "Move the camera away", ModCategory.OTHER);
	}

	@Override
	public void setup() {
		this.addSliderSetting("Range", this, 20, 1, 50, true);
	}
	
	@EventTarget
	public void onCameraRotation(EventCameraRotation event) {
		
		int range = HackSoar.instance.settingsManager.getSettingByName(this, "Range").getValInt();
		
		if(mc.gameSettings.thirdPersonView == 1) {
			GlStateManager.translate(0.0F, 0.0F, -range);
		}
	}
}
