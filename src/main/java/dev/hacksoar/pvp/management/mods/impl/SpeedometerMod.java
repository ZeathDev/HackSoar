package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventRenderShadow;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.color.ColorUtils;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.player.PlayerUtils;
import dev.hacksoar.utils.render.RenderUtils;
import dev.hacksoar.utils.render.RoundedUtils;
import dev.hacksoar.utils.render.StencilUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class SpeedometerMod extends Mod {

	private final int speedCount = 200;
	private final double[] speeds = new double[speedCount];
	private long lastUpdate;
	private static final DecimalFormat speedFormat = new DecimalFormat("0.00");
	
	public SpeedometerMod() {
		super("Speedometer", "Display player speed", ModCategory.HUD);
	}
	
	@Override
	public void setup() {
		this.addBooleanSetting("Graph", this, true);
	}
	
	private void addSpeed(double speed) {
		
		if(speed > 3.8) {
			speed = 3.8;
		}
		
		System.arraycopy(speeds, 1, speeds, 0, speedCount - 1);
		speeds[speedCount - 1] = speed;
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		boolean graphMode = HackSoar.instance.settingsManager.getSettingByName(this, "Graph").getValBoolean();
		
		if(graphMode) {
			this.drawBackground(this.getX(), this.getY(), 155, 100);
			
			FontUtils.regular_bold26.drawString("Speed: " + speedFormat.format(PlayerUtils.getSpeed()) + " m/s", this.getX() + 4.5F, this.getY() + 4.5F, this.getFontColor().getRGB());
			
	        StencilUtils.initStencilToWrite();
	        RenderUtils.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), -1);
	        StencilUtils.readStencilBuffer(1);
			
			RoundedUtils.drawRound(this.getX(), this.getY() + 20, 155, 1, 0, this.getFontColor());
			
			double[] speeds = this.speeds;
			
			GL11.glLineWidth(2.5F);
			if(!mc.isGamePaused() && (lastUpdate == -1 || (System.currentTimeMillis() - lastUpdate) > 30)) {
				addSpeed(PlayerUtils.getSpeed() / 5);
				lastUpdate = System.currentTimeMillis();
			}
			
			GlStateManager.enableBlend();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glShadeModel(GL11.GL_SMOOTH);

			GL11.glBegin(GL11.GL_LINE_STRIP);
			
			ColorUtils.setColor(this.getFontColor().getRGB());
			for(int i = 0; i < speedCount; i++) {
				GL11.glVertex2d((this.getWidth() * i) / (double) speedCount + 3,
						this.getHeight() - (speeds[i] * 16) - 10);
			}

			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			ColorUtils.resetColor();
			
	        StencilUtils.uninitStencilBuffer();
			
			this.setWidth(155);
			this.setHeight(100);
			
		}else {
			this.drawBackground(this.getX(), this.getY(), (float) (FontUtils.regular_bold20.getStringWidth(speedFormat.format(PlayerUtils.getSpeed()) + " m/s") + 9), FontUtils.regular20.getHeight() + 7.5F);
			FontUtils.regular_bold20.drawString(speedFormat.format(PlayerUtils.getSpeed()) + " m/s", this.getX() + 4.5F, this.getY() + 4F, this.getFontColor().getRGB());
			
			this.setWidth((int) (FontUtils.regular_bold20.getStringWidth(speedFormat.format(PlayerUtils.getSpeed()) + " m/s") + 8F));
			this.setHeight((int) (FontUtils.regular20.getHeight() + 8F));
		}
	}
	
	@EventTarget
	public void onRenderShadow(EventRenderShadow event) {
		this.drawShadow(this.getX(), this.getY(), this.getWidth() - this.getX(), this.getHeight() - this.getY());
	}
}