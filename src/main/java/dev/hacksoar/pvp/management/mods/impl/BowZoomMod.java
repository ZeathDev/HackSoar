package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventFovUpdate;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.player.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BowZoomMod extends Mod {

	public BowZoomMod() {
		super("Bow Zoom", "Zooming when using a bow", ModCategory.RENDER);
	}

	@Override
	public void setup() {
		this.addSliderSetting("Factor", this, 5, 1, 15, true);
	}
	
	@EventTarget
	public void onFovUpdate(EventFovUpdate event) {
		
		float base = 1.0F;
		EntityPlayer entity = event.getEntity();
		ItemStack item = entity.getItemInUse();
		int useDuration = entity.getItemInUseDuration();
		
		float bowFov = HackSoar.instance.settingsManager.getSettingByName(this, "Factor").getValInt();
		
		if(item != null && item.getItem() == Items.bow) {
			int duration = (int) Math.min(useDuration, 20.0F);
			float modifier = PlayerUtils.MODIFIER_BY_TICK.get(duration);
			base-= modifier * bowFov;
			event.setFov(base);
		}
	}
}
