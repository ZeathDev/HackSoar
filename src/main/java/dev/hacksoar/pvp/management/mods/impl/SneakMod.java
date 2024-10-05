package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventKey;
import dev.hacksoar.api.events.impl.EventUpdate;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;

public class SneakMod extends Mod {

	private boolean toggle;
	
	public SneakMod() {
		super("Sneak", "Always sneak with one press", ModCategory.PLAYER);
	}
	
	@Override
	public void setup() {
		toggle = false;
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		if(event.getKey() == mc.gameSettings.keyBindSneak.getKeyCode()) {
			toggle = !toggle;
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(toggle) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
		}else {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
		}
		
		if(mc.currentScreen instanceof Gui) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		toggle = false;
	}
}
