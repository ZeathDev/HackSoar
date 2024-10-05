package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventKey;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;

public class ClickGUIMod extends Mod {

	public ClickGUIMod() {
		super("ClickGUI", "Show client settings", ModCategory.OTHER);
	}

	@Override
	public void setup() {
		this.setHide(true);
		this.setToggled(true);
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		if(event.getKey() == HackSoar.instance.keyBindManager.CLICKGUI.getKeyCode()) {
	    	mc.displayGuiScreen(HackSoar.instance.guiManager.getClickGUI());
		}
		if(event.getKey() == HackSoar.instance.keyBindManager.HACKCLICKGUI.getKeyCode()) {
			mc.displayGuiScreen(HackSoar.instance.guiManager.getgClickGUI());
		}
	}
	
    @Override
    public void onDisable() {
        super.onEnable();
        this.setToggled(true);
    }
}
