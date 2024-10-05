package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventCameraRotation;
import dev.hacksoar.api.events.impl.EventKey;
import dev.hacksoar.api.events.impl.EventPlayerHeadRotation;
import dev.hacksoar.api.events.impl.EventTick;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.ClientUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class FreelookMod extends Mod {

	private boolean active;
	private float yaw;
	private float pitch;
	private int previousPerspective;
	private boolean toggled;
	
	public FreelookMod() {
		super("Freelook", "Move the viewpoint freely", ModCategory.PLAYER);
	}
	
	@Override
	public void setup() {
		
		ArrayList<String> options = new ArrayList<String>();
		
		options.add("Toggle");
		options.add("Keydown");
		
		this.addBooleanSetting("Invert", this, false);
		this.addModeSetting("Mode", this, "Keydown", options);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		ClientUtils.showNotification("Warning", "This mod is banned on some servers");
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		
		String mode = HackSoar.instance.settingsManager.getSettingByName(this, "Mode").getValString();
		
		if(mode.equals("Keydown")) {
			if(HackSoar.instance.keyBindManager.FREELOOK.isKeyDown()) {
				start();
			}
			else {
				stop();
			}
		}
		
		if(mode.equals("Toggle")) {
			if(toggled) {
				start();
			}else {
				stop();
			}
		}
	}
	
	@EventTarget
	public void onKey(EventKey event) {
		
		String mode = HackSoar.instance.settingsManager.getSettingByName(this, "Mode").getValString();
		
		if(mode.equals("Toggle")) {
			if(HackSoar.instance.keyBindManager.FREELOOK.isPressed()) {
				toggled = !toggled;
			}
		}
		
		if(Keyboard.getEventKey() == mc.gameSettings.keyBindTogglePerspective.getKeyCode()) {
			toggled = false;
		}
	}
	
	@EventTarget
	public void onCameraRotation(EventCameraRotation event) {
		if(active) {
			event.setYaw(yaw);
			event.setPitch(pitch);
		}
	}
	
	@EventTarget
	public void onPlayerHeadRotation(EventPlayerHeadRotation event) {
		
		boolean invert = HackSoar.instance.settingsManager.getSettingByName(this, "Invert").getValBoolean();
		
		if(active) {
			float yaw = event.getYaw();
			float pitch = event.getPitch();
			event.setCancelled(true);
			pitch = -pitch;
			
			if(!invert) {
				pitch = -pitch;
			}
			
			if(invert) {
				 yaw = -yaw;
			}
			
			this.yaw += yaw * 0.15F;
			this.pitch = MathHelper.clamp_float(this.pitch + (pitch * 0.15F), -90, 90);
			mc.renderGlobal.setDisplayListEntitiesDirty();
		}
	}
	
	private void start() {
		if(!active) {
			active = true;
			previousPerspective = mc.gameSettings.thirdPersonView;
			mc.gameSettings.thirdPersonView = 3;
			Entity renderView = mc.getRenderViewEntity();
			yaw = renderView.rotationYaw;
			pitch = renderView.rotationPitch;
		}
	}
	
	private void stop() {
		if(active) {
			active = false;
			mc.gameSettings.thirdPersonView = previousPerspective;
			mc.renderGlobal.setDisplayListEntitiesDirty();
		}
	}
}
