package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRenderBossbar;
import dev.hacksoar.pvp.GuiEditHUD;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;

public class BossbarMod extends Mod {

	private int offset = 13;
	protected float zLevelFloat;
	
	public BossbarMod() {
		super("Bossbar", "Display the bossbar", ModCategory.HUD);
	}

	@EventTarget
	public void onRenderBossbar(EventRenderBossbar event) {
		if(mc.currentScreen instanceof GuiEditHUD) {
			this.renderDammy();
		}else {
			this.render();
		}
		
		this.setWidth(182);
		this.setHeight(18);
	}
	
	private void render() {
		if (BossStatus.bossName != null && BossStatus.statusBarTime > 0){
            this.mc.getTextureManager().bindTexture(Gui.icons);
            --BossStatus.statusBarTime;
            this.mc.getTextureManager().bindTexture(Gui.icons);
            int j = 182;
            this.mc.getTextureManager().bindTexture(Gui.icons);
            int l = (int)(BossStatus.healthScale * (float)(j + 1));
            RenderUtils.drawTexturedModalRect(this.getX(), this.getY() + offset, 0, 74, j, 5);
            RenderUtils.drawTexturedModalRect(this.getX(), this.getY() + offset, 0, 74, j, 5);
            
            if (l > 0)
            {
            	RenderUtils.drawTexturedModalRect(this.getX(), this.getY() + offset, 0, 79, l, 5);
            }
            
            this.mc.getTextureManager().bindTexture(Gui.icons);
            
            String s = BossStatus.bossName;
            
            fr.drawStringWithShadow(s, (((182 / 2) - (fr.getStringWidth(s) / 2)) + this.getX()), (this.getY() - 10) + offset, 16777215);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(Gui.icons);
        }
	}
	
	private void renderDammy() {
		this.mc.getTextureManager().bindTexture(Gui.icons);
        --BossStatus.statusBarTime;
        this.mc.getTextureManager().bindTexture(Gui.icons);
        int j = 182;
        this.mc.getTextureManager().bindTexture(Gui.icons);
        int l = (int)(BossStatus.healthScale * (float)(j + 1));
        RenderUtils.drawTexturedModalRect(this.getX(), this.getY() + offset + 1, 0, 74, j, 5);
        RenderUtils. drawTexturedModalRect(this.getX(), this.getY() + offset + 1, 0, 74, j, 5);
        if (l > 0)
        {
        	RenderUtils.drawTexturedModalRect(this.getX(), this.getY() + offset + 1, 0, 79, l, 5);
        }
        this.mc.getTextureManager().bindTexture(Gui.icons);
        
        String s = "Bossbar";
        
        fr.drawStringWithShadow(s, (((182 / 2) - (fr.getStringWidth(s) / 2)) + this.getX()), (this.getY() - 10) + offset, 16777215);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Gui.icons);	
	}
}
