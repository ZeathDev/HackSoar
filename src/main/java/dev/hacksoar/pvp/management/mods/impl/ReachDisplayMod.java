package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventDamageEntity;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.management.mods.HudMod;
import net.minecraft.util.MovingObjectPosition;

import java.text.DecimalFormat;

public class ReachDisplayMod extends HudMod{

	private DecimalFormat format = new DecimalFormat("0.##");
	
	private double distance = 0;
	private long hitTime =  -1;
	
	public ReachDisplayMod() {
		super("Reach Display", "Display your reach");
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		super.onRender2D();
	}
	
	@EventTarget
	public void onRenderShadow(EventRenderShadow event) {
		super.onRenderShadow();
	}
	
	@EventTarget
	public void onDamageEntity(EventDamageEntity event) {
		if(mc.objectMouseOver != null && mc.objectMouseOver.hitVec != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
			distance = mc.objectMouseOver.hitVec.distanceTo(mc.thePlayer.getPositionEyes(1.0F));
			hitTime = System.currentTimeMillis();
		}
	}
	
	@Override
	public String getText() {
		if((System.currentTimeMillis() - hitTime) > 5000) {
			distance = 0;
		}
		if(distance == 0) {
			return "Hasn't attacked";
		}else {
			return format.format(distance) + " blocks";
		}
	}
}
