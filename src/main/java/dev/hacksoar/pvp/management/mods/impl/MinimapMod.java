package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.ui.minimap.XaeroMinimap;
import dev.hacksoar.ui.minimap.animation.MinimapAnimation;
import dev.hacksoar.ui.minimap.interfaces.InterfaceHandler;
import dev.hacksoar.utils.ClientUtils;
import dev.hacksoar.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class MinimapMod extends Mod {

	private XaeroMinimap minimap = new XaeroMinimap();
	
	public MinimapMod() {
		super("Minimap", "Display minimap", ModCategory.HUD);
	}

	@EventTarget
	public void onRender2D(EventRender2D event) {
        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        InterfaceHandler.drawInterfaces((Minecraft.getMinecraft()).getTimer().renderPartialTicks);
        MinimapAnimation.tick();
        
        this.setWidth(75);
        this.setHeight(75);
	}
	
	@EventTarget
	public void onRenderShadow(EventRenderShadow event) {
		RenderUtils.drawRect(this.getX(), this.getY(), 75, 75, Color.BLACK.getRGB());
	}
	
	@Override
	public void onEnable() {
		
		super.onEnable();
		
		if(!ClientUtils.loadedMinimap) {
			
			ClientUtils.loadedMinimap = true;
			
			try {
				minimap.load();
			} catch (IOException e) {
				// Don't give crackers clues...
				if (HackSoar.instance.DEVELOPMENT_SWITCH)
					e.printStackTrace();
			}
		}
	}
}
