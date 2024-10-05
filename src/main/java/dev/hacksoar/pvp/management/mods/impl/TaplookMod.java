package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventTick;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

import java.util.ArrayList;

public class TaplookMod extends Mod {

	private boolean active;
	private int prevPerspective;
	
	public TaplookMod() {
		super("Taplook", "Switch viewing orientation at the touch of a button", ModCategory.PLAYER);
	}

	@Override
	public void setup() {
		
		ArrayList<String> options = new ArrayList<String>();
		
		options.add("Front");
		options.add("Behind");
		
		this.addModeSetting("Perspective", this, "Front", options);
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if(HackSoar.instance.keyBindManager.TAPLOOK.isKeyDown()) {
			if(!active) {
				this.start();
			}
		}else if(active) {
			this.stop();
		}
	}
	
	private void start() {
		
		String type = HackSoar.instance.settingsManager.getSettingByName(this, "Perspective").getValString();
		int perspective = 0;
		
		active = true;
		prevPerspective = mc.gameSettings.thirdPersonView;
		
		switch(type) {
			case "Front":
				perspective = 2;
				break;
			case "Behind":
				perspective = 1;
				break;
		}
		
		mc.gameSettings.thirdPersonView = perspective;
		mc.renderGlobal.setDisplayListEntitiesDirty();
	}
	
	private void stop() {
		active = false;
		mc.gameSettings.thirdPersonView = prevPerspective;
		mc.renderGlobal.setDisplayListEntitiesDirty();
	}
}
