package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.font.FontUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;

import java.text.DecimalFormat;

public class HorseStatsMod extends Mod {

	private DecimalFormat df = new DecimalFormat("0.0");
	
	public HorseStatsMod() {
		super("Horse Stats", "Display horse stats", ModCategory.HUD);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		String speed = "Speed: 0.0 b/s";
		String jump = "Jump: 0.0 Blocks";
		
		this.drawBackground(this.getX(), this.getY(), 90 + 4.5F, 26.5F);
		
		if(mc.objectMouseOver.entityHit instanceof EntityHorse) {
			EntityHorse horse = (EntityHorse) mc.objectMouseOver.entityHit;
			
			if(!mc.thePlayer.isRidingHorse()) {
				speed = "Speed: " + this.getHorseSpeedRounded(horse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()) + " b/s";
				jump = "Jump: " + df.format(horse.getHorseJumpStrength() * 5.5) + " Blocks";
			}
		}
		
		FontUtils.regular_bold20.drawString(speed, this.getX() + 4.5F, this.getY() + 4.5F, this.getFontColor().getRGB());
		FontUtils.regular_bold20.drawString(jump, this.getX() + 4.5F, this.getY() + 14.5F, this.getFontColor().getRGB());
		
		this.setWidth((int) (90 + 5F));
		this.setHeight((int) 26.5F);
	}
	
	@EventTarget
	public void onRenderShadow(EventRenderShadow event) {
		this.drawShadow(this.getX(), this.getY(), 90 + 4.5F, 26.5F);
	}
	
    private String getHorseSpeedRounded(double baseSpeed) {
        final float factor = 43.1703703704f;
        
        float speed = (float) (baseSpeed * factor);
        
        return df.format(speed);
    }
}
