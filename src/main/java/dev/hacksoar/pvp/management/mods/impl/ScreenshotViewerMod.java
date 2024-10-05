package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventKey;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class ScreenshotViewerMod extends Mod {

	public ScreenshotViewerMod() {
		super("Screenshot Viewer", "View a list of screenshots", ModCategory.RENDER);
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		if(event.getKey() == HackSoar.instance.keyBindManager.SCREENSHOT_VIEWER.getKeyCode()) {
			mc.displayGuiScreen(HackSoar.instance.guiManager.getGuiScreenshotViewer());
		}
	}
}
