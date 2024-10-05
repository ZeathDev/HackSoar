package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventAttackEntity;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;

import java.util.ArrayList;

public class ParticlesMod extends Mod {

	public ParticlesMod() {
		super("Particles", "Particle Customization", ModCategory.RENDER);
	}

	@Override
	public void setup() {
		
		ArrayList<String> options = new ArrayList<String>();
		
		options.add("Slime");
		options.add("Note");
		options.add("Redstone");
		options.add("Portal");
		
		this.addBooleanSetting("Always Sharpness", this, false);
		this.addBooleanSetting("Sharpness", this, true);
		this.addBooleanSetting("Criticals", this, false);
		this.addSliderSetting("Multiplier", this, 2, 1, 10, true);
		
		this.addBooleanSetting("Custom Particle", this, false);
		this.addModeSetting("Particle Type", this, "Slime", options);
		this.addSliderSetting("Custom Particle Multiplier", this, 2, 1, 10, true);
	}
	
	@EventTarget
	public void onAttackEntity(EventAttackEntity event) {
		
		EntityPlayer player = mc.thePlayer;
		
		int multiplier = (int) HackSoar.instance.settingsManager.getSettingByName(this, "Multiplier").getValDouble();
		
		if(!(event.getEntity() instanceof EntityLivingBase)) {
			return;
		}
		
		boolean critical = HackSoar.instance.settingsManager.getSettingByName(this, "Criticals").getValBoolean() && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null;
		boolean alwaysSharpness = HackSoar.instance.settingsManager.getSettingByName(this, "Always Sharpness").getValBoolean();
		boolean sharpness = HackSoar.instance.settingsManager.getSettingByName(this, "Sharpness").getValBoolean() && EnchantmentHelper.getModifierForCreature(player.getHeldItem(), ((EntityLivingBase) event.getEntity()).getCreatureAttribute()) > 0;
		
		if(critical == true) {
			for(int i = 0; i < multiplier - 1; i++) {
				mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.CRIT);
			}
		}
		
		if(alwaysSharpness == true || sharpness == true) {
			for(int i = 0; i < multiplier - 1; i++) {
				mc.effectRenderer.emitParticleAtEntity(event.getEntity(), EnumParticleTypes.CRIT_MAGIC);
			}
		}
		
		boolean customParticle = HackSoar.instance.settingsManager.getSettingByName(this, "Custom Particle").getValBoolean();
		String particleType = HackSoar.instance.settingsManager.getSettingByName(this, "Particle Type").getValString();
		int customParticleMultiplier = HackSoar.instance.settingsManager.getSettingByName(this, "Custom Particle Multiplier").getValInt();
		EnumParticleTypes enumParticleType = EnumParticleTypes.SLIME;
		
		if(customParticle) {
			switch(particleType) {
				case "Slime":
					enumParticleType = EnumParticleTypes.SLIME;
					break;
				case "Note":
					enumParticleType = EnumParticleTypes.NOTE;
					break;
				case "Portal":
					enumParticleType = EnumParticleTypes.PORTAL;
					break;
				case "Redstone":
					enumParticleType = EnumParticleTypes.REDSTONE;
					break;
			}
			
			for(int i = 0; i < customParticleMultiplier - 1; i++) {
				mc.effectRenderer.emitParticleAtEntity(event.getEntity(), enumParticleType);
			}
		}
	}
}
