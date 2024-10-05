package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventFovUpdate;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

public class FovModifierMod extends Mod {
    
	public FovModifierMod() {
		super("Fov Modifier", "Modify fov", ModCategory.RENDER);
	}
	
	@Override
	public void setup() {
		this.addSliderSetting("Sprinting", this, 1, -5, 5, false);
		this.addSliderSetting("Bow", this, 1, -5, 5, false);
		this.addSliderSetting("Speed", this, 1, -5, 5, false);
		this.addSliderSetting("Slowness", this, 1, -5, 5, false);
	}
	
	@EventTarget
	public void onFovUpdate(EventFovUpdate event) {
		
		float base = 1.0F;
		EntityPlayer entity = event.getEntity();
		ItemStack item = entity.getItemInUse();
		int useDuration = entity.getItemInUseDuration();
		
		float sprintingFov = (float) HackSoar.instance.settingsManager.getSettingByName(this, "Sprinting").getValDouble();
		float bowFov = (float) HackSoar.instance.settingsManager.getSettingByName(this, "Bow").getValDouble();
		float speedFov = (float) HackSoar.instance.settingsManager.getSettingByName(this, "Speed").getValDouble();
		float slownessFov = (float) HackSoar.instance.settingsManager.getSettingByName(this, "Slowness").getValDouble();
		
		if(entity.isSprinting()) {
			base += 0.15000000596046448  * sprintingFov;
		}
		
		if(item != null && item.getItem() == Items.bow) {
			int duration = (int) Math.min(useDuration, 20.0F);
			float modifier = PlayerUtils.MODIFIER_BY_TICK.get(duration);
			base-= modifier * bowFov;
		}
		
        Collection<PotionEffect> effects = entity.getActivePotionEffects();
        if (!effects.isEmpty()) {
            for (PotionEffect effect : effects) {
                int potionID = effect.getPotionID();
                if (potionID == 1) {
                    base += 0.1F * (effect.getAmplifier() + 1) * speedFov;
                }

                if (potionID == 2) {
                    base += -0.075F * (effect.getAmplifier() + 1) * slownessFov;
                }
            }
        }
        
        event.setFov(base);
	}
}
