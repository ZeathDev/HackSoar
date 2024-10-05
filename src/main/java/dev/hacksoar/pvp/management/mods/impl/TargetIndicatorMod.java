package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender3D;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.TargetUtils;
import dev.hacksoar.utils.render.RenderUtils;

public class TargetIndicatorMod extends Mod {

	public TargetIndicatorMod() {
		super("Target Indicator", "Indicates the current target", ModCategory.RENDER);
	}

	@EventTarget
	public void onRender3D(EventRender3D event) {
		if(TargetUtils.getTarget() != null) {
			
			if(TargetUtils.getTarget().equals(mc.thePlayer)) {
				return;
			}

			RenderUtils.drawTargetCapsule(TargetUtils.getTarget(), 0.67, true);
		}
	}
}
