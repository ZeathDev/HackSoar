package dev.hacksoar.utils.render;

import dev.hacksoar.HackSoar;
import dev.hacksoar.pvp.management.mods.impl.ClickEffectMod;
import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import dev.hacksoar.utils.color.ColorUtils;

import java.awt.*;

public class ClickEffect {
	
	private float x, y;
	
	private SimpleAnimation animation = new SimpleAnimation(0.0F);
	
	public ClickEffect(float x, float y) {
		this.x = x;
		this.y = y;
		animation.setValue(0);
	}
	
	public void draw() {
		animation.setAnimation(100, 12);
        double radius = 8 * animation.getValue() / 100;
        int alpha = (int)(255 - 255 * animation.getValue() / 100);
        boolean accentColor = HackSoar.instance.settingsManager.getSettingByClass(ClickEffectMod.class, "Accent Color").getValBoolean();
        int color = accentColor ? ColorUtils.getClientColor(0, alpha).getRGB() : new Color(255, 255, 255, alpha).getRGB();
        
        if(HackSoar.instance.modManager.getModByClass(ClickEffectMod.class).isToggled()) {
            RenderUtils.drawArc(x, y, radius, color, 0, 360, 5);
        }
	}
	
	public boolean canRemove() {
		return animation.getValue() > 99;
	}
}
