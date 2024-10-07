package dev.hacksoar.pvp.hooks;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderDamageTint;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.GuiEditHUD;
import dev.hacksoar.pvp.management.mods.impl.HUDMod;
import dev.hacksoar.pvp.management.mods.impl.MenuBlurMod;
import dev.hacksoar.ui.notification.NotificationManager;
import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import dev.hacksoar.utils.color.ColorUtils;
import dev.hacksoar.utils.render.GlUtils;
import dev.hacksoar.utils.render.RoundedUtils;
import dev.hacksoar.utils.render.StencilUtils;
import dev.hacksoar.utils.shader.BlurUtils;
import dev.hacksoar.utils.shader.GaussianBlur;
import dev.hacksoar.utils.shader.ShadowUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

import static dev.hacksoar.HackSoar.mc;

public class GuiIngameHook {

	private static Framebuffer shadowFramebuffer = new Framebuffer(1, 1, false);
	private static final SimpleAnimation opacityAnimation = new SimpleAnimation(0.0F);
	
	public static void renderGameOverlay(float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		opacityAnimation.setAnimation(Minecraft.getMinecraft().currentScreen instanceof GuiEditHUD ? 220 : 0, 16);
		
		RoundedUtils.drawRound(0, sr.getScaledHeight() / 2, sr.getScaledWidth(), 1, 0, ColorUtils.getBackgroundColor(4, (int) opacityAnimation.getValue()));
		RoundedUtils.drawRound(sr.getScaledWidth() / 2, 0, 1, sr.getScaledHeight(), 0, ColorUtils.getBackgroundColor(4, (int) opacityAnimation.getValue()));
		
		EventRenderDamageTint event3 = new EventRenderDamageTint();
		event3.call();
		
		if((!HackSoar.instance.settingsManager.getSettingByClass(HUDMod.class, "Hide Debug Menu").getValBoolean()) || (HackSoar.instance.settingsManager.getSettingByClass(HUDMod.class, "Hide Debug Menu").getValBoolean() && !Minecraft.getMinecraft().gameSettings.showDebugInfo)) {
			EventRender2D event = new EventRender2D(partialTicks, mc.scaledresolution);
			EventRenderShadow event2 = new EventRenderShadow(partialTicks);

			if (HackSoar.instance.settingsManager.getSettingByClass(HUDMod.class, "Shadow").getValBoolean()) {

				shadowFramebuffer = GlUtils.createFrameBuffer(shadowFramebuffer);

				shadowFramebuffer.framebufferClear();
				shadowFramebuffer.bindFramebuffer(true);
				event2.call();
				shadowFramebuffer.unbindFramebuffer();

				ShadowUtils.renderShadow(shadowFramebuffer.framebufferTexture, 8, 2);
			}
	        
	        if(HackSoar.instance.settingsManager.getSettingByClass(HUDMod.class, "Blur").getValBoolean()) {
				StencilUtils.initStencilToWrite();
	            event2.call();
				StencilUtils.readStencilBuffer(1);
				GaussianBlur.renderBlur(HackSoar.instance.settingsManager.getSettingByClass(HUDMod.class, "Blur Radius").getValInt());
				StencilUtils.uninitStencilBuffer();
	        }
	        
	        event.call();
		}
		
		if(Minecraft.getMinecraft().currentScreen != null) {
	        if(HackSoar.instance.modManager.getModByClass(MenuBlurMod.class).isToggled()) {
	        	BlurUtils.drawBlurScreen();
	        }	
		}
		
		NotificationManager.render();
        
        GlStateManager.enableBlend();
	}
}
