package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.management.mods.HudMod;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class CPSDisplayMod extends HudMod{

    private List<Long> clicksLMB = new ArrayList<Long>();
    
    private List<Long> clicksRMB = new ArrayList<Long>();
    
    private boolean wasPressedLMB;
    private long lastPressedLMB;
    
    private boolean wasPressedRMB;
    private long lastPressedRMB;
    
	public CPSDisplayMod() {
		super("CPS Display", "Display click per secound");
	}

	@Override
	public void setup() {
		this.addBooleanSetting("Show Right Click", this, true);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		super.onRender2D();
	}
	
	@EventTarget
	public void onRenderShadow(EventRenderShadow event) {
		super.onRenderShadow();
	}
	
	@Override
	public String getText() {
		
		boolean rightClick = HackSoar.instance.settingsManager.getSettingByName(this, "Show Right Click").getValBoolean();
        boolean pressedLMB = Mouse.isButtonDown(0);
        
        if(pressedLMB != this.wasPressedLMB) {
            this.lastPressedLMB = System.currentTimeMillis();
            this.wasPressedLMB = pressedLMB;
            if(pressedLMB) {
                this.clicksLMB.add(this.lastPressedLMB);
            }
        }
        
        boolean pressedRMB = Mouse.isButtonDown(1);
        
        if(pressedRMB != this.wasPressedRMB) {
            this.lastPressedRMB = System.currentTimeMillis();
            this.wasPressedRMB = pressedRMB;
            if(pressedRMB) {
                this.clicksRMB.add(this.lastPressedRMB);
            }
        }
        
		return (rightClick ? this.getLMB() + " | " + this.getRMB() : this.getLMB()) + " CPS";
	}
	
    public int getLMB() {
        final long time = System.currentTimeMillis();
        this.clicksLMB.removeIf(aLong -> aLong + 1000 < time);
        return this.clicksLMB.size();
    }
    
    public int getRMB() {
        final long time = System.currentTimeMillis();
        this.clicksRMB.removeIf(aLong -> aLong + 1000 < time);
        return this.clicksRMB.size();
    }
}
