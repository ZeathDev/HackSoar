package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.api.events.impl.EventRenderTick;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.TimerUtils;
import dev.hacksoar.utils.render.RenderGlobalHelper;
import dev.hacksoar.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.IntBuffer;

public class RearviewMod extends Mod {

	private int mirrorFBO;
	private int mirrorTex;
	private int mirrorDepth;
	private long renderEndNanoTime;
	private RenderGlobalHelper mirrorRenderGlobal;
	private TimerUtils timer = new TimerUtils();
	private boolean firstUpdate;
	
	public RearviewMod() {
		super("Rearview", "Display behind you", ModCategory.HUD);
	}

	@Override
	public void setup() {

		this.addBooleanSetting("Damage Shake", this, false);
		this.addSliderSetting("Fov", this, 70, 30, 110, true);
		this.addSliderSetting("FPS", this, 30, 1, 240, true);
		this.addSliderSetting("Width", this, 350, 10, 500, true);
		this.addSliderSetting("Height", this, 150, 10, 500, true);
		this.addBooleanSetting("Lock Camera", this, true);
		
        mirrorFBO = ARBFramebufferObject.glGenFramebuffers();
        mirrorTex = GL11.glGenTextures();
        mirrorDepth = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mirrorTex);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, 320, 180, 0, GL11.GL_RGBA, GL11.GL_INT,
                (IntBuffer) null);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mirrorDepth);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, 320, 180, 0, GL11.GL_DEPTH_COMPONENT,
                GL11.GL_INT, (IntBuffer) null);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        mirrorRenderGlobal = new RenderGlobalHelper();
	}
	
	@EventTarget
	public void onRenderTick(EventRenderTick event) {
		
		int fps = HackSoar.instance.settingsManager.getSettingByName(this, "FPS").getValInt();
		
        if(mc.thePlayer != null && mc.thePlayer != null && Display.isActive()) {
        	if (timer.delay((long) (1000 / fps))) {
                updateMirror();
                timer.reset();
            }
        }
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		int width = HackSoar.instance.settingsManager.getSettingByName(this, "Width").getValInt();
		int height = HackSoar.instance.settingsManager.getSettingByName(this, "Height").getValInt();
        Tessellator tes = Tessellator.getInstance();
        WorldRenderer wr = tes.getWorldRenderer();
        
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int factor = sr.getScaleFactor();
        
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_CURRENT_BIT | GL11.GL_POLYGON_BIT | GL11.GL_TEXTURE_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, mc.displayWidth, mc.displayHeight, 0, 1000, 3000);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0, 0, -2000);
		
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        
        GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mirrorTex);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.pos(this.getX() * factor, this.getY() * factor + height, 0).tex(1, 0).endVertex();
        wr.pos(this.getX() * factor + width, this.getY() * factor + height, 0).tex(0, 0).endVertex();
        wr.pos(this.getX() * factor + width, this.getY() * factor, 0).tex(0, 1).endVertex();
        wr.pos(this.getX() * factor, this.getY() * factor, 0).tex(1, 1).endVertex();
        tes.draw();
        
        GlStateManager.bindTexture(0);
        
        GL11.glPopAttrib();
        
        mc.entityRenderer.setupOverlayRendering();
        
        this.setWidth(width / factor);
        this.setHeight(height / factor);
	}
	
	@EventTarget
	public void onRenderShadow(EventRenderShadow event) {
		
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int factor = sr.getScaleFactor();
        
		int width = HackSoar.instance.settingsManager.getSettingByName(this, "Width").getValInt();
		int height = HackSoar.instance.settingsManager.getSettingByName(this, "Height").getValInt();
		
		RenderUtils.drawRect(this.getX(), this.getY(), width / factor, height / factor, Color.BLACK.getRGB());
	}
	
    private void updateMirror() {
    	
        int w, h;
        float y, py, p, pp;
        boolean hide;
        int view, limit;
        long endTime = 0;
        float fov;
        boolean lockCamera = HackSoar.instance.settingsManager.getSettingByName(this, "Lock Camera").getValBoolean();
        boolean damageShake;
        
        GuiScreen currentScreen;
        
        if (!this.firstUpdate) {
            mc.renderGlobal.loadRenderers();
            this.firstUpdate = true;
        }
        
        w = mc.displayWidth;
        h = mc.displayHeight;
        y = (mc).getRenderViewEntity().rotationYaw;
        py = (mc).getRenderViewEntity().prevRotationYaw;
        p = (mc).getRenderViewEntity().rotationPitch;
        pp = (mc).getRenderViewEntity().prevRotationPitch;
        hide = mc.gameSettings.hideGUI;
        view = mc.gameSettings.thirdPersonView;
        limit = mc.gameSettings.limitFramerate;
        fov = mc.gameSettings.fovSetting;
        currentScreen = mc.currentScreen;
        damageShake = HackSoar.instance.modManager.getModByClass(MinimalDamageShakeMod.class).isToggled();
    	
        switchToFB();

        if (limit != 0) {
        	endTime = renderEndNanoTime;
        }

        mc.currentScreen = null;
        mc.displayHeight = 180;
        mc.displayWidth = 320;
        mc.gameSettings.hideGUI = true;
        mc.gameSettings.thirdPersonView = 0;
        mc.gameSettings.limitFramerate = 0;
        mc.gameSettings.fovSetting = HackSoar.instance.settingsManager.getSettingByName(this, "Fov").getValFloat();

        if(!damageShake) {
        	if(!HackSoar.instance.settingsManager.getSettingByName(this, "Damage Shake").getValBoolean()) {
            	HackSoar.instance.modManager.getModByClass(MinimalDamageShakeMod.class).setToggled(true);
        	}
        }else {
        	HackSoar.instance.modManager.getModByClass(MinimalDamageShakeMod.class).setToggled(false);
        }
        
        (mc).getRenderViewEntity().rotationYaw += 180;
        (mc).getRenderViewEntity().prevRotationYaw += 180;
        
        if(lockCamera) {
            (mc).getRenderViewEntity().rotationPitch = 0;
            (mc).getRenderViewEntity().prevRotationPitch = 0;
        }else {
            (mc).getRenderViewEntity().rotationPitch = -p + 18;
            (mc).getRenderViewEntity().prevRotationPitch = -pp + 18;
        }

        mirrorRenderGlobal.switchTo();

        GL11.glPushAttrib(272393);
        
        mc.entityRenderer.renderWorld((mc).getTimer().renderPartialTicks, System.nanoTime());
        mc.entityRenderer.setupOverlayRendering();
        
        if (limit != 0) {
        	renderEndNanoTime = endTime;
        }
        
        GL11.glPopAttrib();
        
        mirrorRenderGlobal.switchFrom();
        
        mc.currentScreen = currentScreen;
        (mc).getRenderViewEntity().rotationYaw = y;
        (mc).getRenderViewEntity().prevRotationYaw = py;
        (mc).getRenderViewEntity().rotationPitch = p;
        (mc).getRenderViewEntity().prevRotationPitch = pp;
        mc.gameSettings.limitFramerate = limit;
        mc.gameSettings.thirdPersonView = view;
        mc.gameSettings.hideGUI = hide;
        mc.displayWidth = w;
        mc.displayHeight = h;
        mc.gameSettings.fovSetting = fov;
    	HackSoar.instance.modManager.getModByClass(MinimalDamageShakeMod.class).setToggled(damageShake);

        switchFromFB();
    }
    
    @Override
    public void onEnable() {
    	super.onEnable();
        this.renderEndNanoTime = (mc.entityRenderer).getRendeerEndNanoTime();
    }
	
    private void switchToFB() {
    	
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		
    	OpenGlHelper.glBindFramebuffer(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER, mirrorFBO);
        OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER,
        		OpenGlHelper.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D,
                mirrorTex, 0);
        OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER,
        		OpenGlHelper.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D,
                mirrorDepth, 0);
    }

    private void switchFromFB() {
    	OpenGlHelper.glBindFramebuffer(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER, 0);
    }
}