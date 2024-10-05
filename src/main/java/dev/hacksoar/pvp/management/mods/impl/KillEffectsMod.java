package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventLoadWorld;
import dev.hacksoar.api.events.impl.EventPreMotion;
import dev.hacksoar.api.events.impl.EventUpdate;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import net.minecraft.block.Block;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class KillEffectsMod extends Mod {

	private EntityLivingBase target;
    private int entityID;
    
	public KillEffectsMod() {
		super("Kill Effects", "Display effects when you kill.", ModCategory.OTHER);
	}

	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<>();
		
		options.add("Lightning");
		options.add("Flames");
		options.add("Cloud");
		options.add("Blood");
		
		this.addBooleanSetting("Sound", this, true);
		this.addModeSetting("Effect", this, "Blood", options);
		this.addSliderSetting("Multiplier", this, 1, 1, 10, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(mc.objectMouseOver != null & mc.objectMouseOver.entityHit != null) {
			if(mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
				target = (EntityLivingBase) mc.objectMouseOver.entityHit;
			}
		}
	}

	@EventTarget
	public void onPreMotionUpdate(EventPreMotion event) {
		
		String mode = HackSoar.instance.settingsManager.getSettingByName(this, "Effect").getValString();
		boolean sound = HackSoar.instance.settingsManager.getSettingByName(this, "Sound").getValBoolean();
		int multiplier = (int) HackSoar.instance.settingsManager.getSettingByName(this, "Multiplier").getValDouble();
		
		if (target != null && !mc.theWorld.loadedEntityList.contains(target) && mc.thePlayer.getDistanceSq(target.posX, mc.thePlayer.posY, target.posZ) < 100) {
			if (mc.thePlayer.ticksExisted > 3) {
				switch(mode) {
					case "Lightning":
                        final EntityLightningBolt entityLightningBolt = new EntityLightningBolt(mc.theWorld, target.posX, target.posY, target.posZ);
                        mc.theWorld.addEntityToWorld(entityID--, entityLightningBolt);

                        if (sound) {
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("ambient.weather.thunder"), ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        }
						break;
					case "Flames":
                        for (int i = 0; i < multiplier; i++) {
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.FLAME);
                        }
                        
                        if (sound) {
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("item.fireCharge.use"), ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        }
						break;
					case "Cloud":
                        for (int i = 0; i < multiplier; i++) {
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CLOUD);
                        }
                        
                        if (sound) {
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("fireworks.twinkle"), ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        }
						break;
					case "Blood":
                        for (int i = 0; i < 50; i++) {
                            mc.theWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height - 0.75, target.posZ, 0, 0, 0, Block.getStateId(Blocks.redstone_block.getDefaultState()));
                        }

                        if (sound) {
                            mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation("dig.stone"), 4.0F, 1.2F, ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        }
						break;
				}
			}
			target = null;
		}
	}
	
	@EventTarget
	public void onLoadWorld(EventLoadWorld event) {
		entityID = 0;
	}
}
