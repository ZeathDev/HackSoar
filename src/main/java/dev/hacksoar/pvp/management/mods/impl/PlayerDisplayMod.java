package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;

public class PlayerDisplayMod extends Mod {

	public PlayerDisplayMod() {
		super("Player Display", "Display you", ModCategory.HUD);
	}

	@Override
	public void setup() {
		this.addSliderSetting("Scale", this, 1.0, 0.1, 3.0, false);
		this.addSliderSetting("Yaw Offset", this, 0, -90, 120, true);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		float raw = HackSoar.instance.settingsManager.getSettingByName(this, "Scale").getValFloat();
		float scale = HackSoar.instance.settingsManager.getSettingByName(this, "Scale").getValFloat() * 30;
		float yawOffset = HackSoar.instance.settingsManager.getSettingByName(this, "Yaw Offset").getValFloat();
		
        GlStateManager.enableColorMaterial();
        GlStateManager.enableDepth();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.getX() + (15 * raw), this.getY() + (58 * raw), -500.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(mc.thePlayer.rotationYaw + yawOffset, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();

        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, event.getPartialTicks(), true);
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        
        this.setWidth((int) (30 * raw));
        this.setHeight((int) (60 * raw));
	}
}
