package dev.hacksoar.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;

public final class RenderGlobalHelper {
    public Minecraft mc;
    public RenderGlobal rg, orig;
    public boolean fancy_graphics;
    public int ambient_occlusion;

    public RenderGlobalHelper() {
        mc = Minecraft.getMinecraft();
        rg = new RenderGlobal(mc);
        orig = null;
    }

    public void getSettings() {
        fancy_graphics = mc.gameSettings.fancyGraphics;
        ambient_occlusion = mc.gameSettings.ambientOcclusion;
    }

    public boolean settingsChanged() {
        return fancy_graphics != mc.gameSettings.fancyGraphics ||
                ambient_occlusion != mc.gameSettings.ambientOcclusion;
    }

    public void switchTo() {
        if (orig == null)
            orig = mc.renderGlobal;
        if ((orig).getWorldClient() != (rg).getWorldClient()) {
            rg.setWorldAndLoadRenderers((orig).getWorldClient());
            getSettings();
        } else if (settingsChanged()) {
            rg.loadRenderers();
            getSettings();
        }
        mc.renderGlobal = rg;
    }

    public void switchFrom() {
        if (orig != null) {
        	mc.renderGlobal = orig;
        }
        orig = null;
    }
}