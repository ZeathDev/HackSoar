package dev.hacksoar.pvp;

import dev.hacksoar.HackSoar;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.pvp.management.mods.impl.ScoreboardMod;
import dev.hacksoar.utils.mouse.MouseUtils;
import dev.hacksoar.utils.render.ClickEffect;
import dev.hacksoar.utils.render.RoundedUtils;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiEditHUD extends GuiScreen{
	
    private List<ClickEffect> clickEffects = new ArrayList<>();

    private boolean fromClickGUI;
    
    public GuiEditHUD(boolean fromClickGUI) {
    	this.fromClickGUI = fromClickGUI;
    }
    
	@Override
	public void initGui() {
		for(Mod m : HackSoar.instance.modManager.getMods()) {
			m.setDragging(false);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(Mod m : HackSoar.instance.modManager.getMods()) {
			if(m.isToggled() && m.getCategory().equals(ModCategory.HUD)) {

				boolean isInside = m.getName().equals("Scoreboard") ?  MouseUtils.isInside(mouseX, mouseY, ScoreboardMod.shadowX, ScoreboardMod.shadowY, ScoreboardMod.shadowWidth - ScoreboardMod.shadowX, ScoreboardMod.shadowHeight - ScoreboardMod.shadowY)
						: MouseUtils.isInside(mouseX, mouseY, m.getX(), m.getY(), m.getWidth() - m.getX(), m.getHeight() - m.getY()) && 
						HackSoar.instance.modManager.getMods().stream().filter(m2 -> m2.isToggled() && m2.getCategory().equals(ModCategory.HUD) && mouseX >= m2.getX() && mouseX <= m2.getWidth() && mouseY >= m2.getY() && mouseY <= m2.getHeight()).findFirst().get().equals(m);
				
				m.editOpacityAnimation.setAnimation(isInside ?  255 : 0, 12);
				
				if(m.getClass().equals(ScoreboardMod.class)) {
					RoundedUtils.drawRoundOutline(ScoreboardMod.shadowX - 4, ScoreboardMod.shadowY - 4, (ScoreboardMod.shadowWidth - ScoreboardMod.shadowX) + 8, (ScoreboardMod.shadowHeight - ScoreboardMod.shadowY) + 8, 6, 1, new Color(255, 255, 255, 0), new Color(255, 255, 255, (int) m.editOpacityAnimation.getValue()));
				}else {
					RoundedUtils.drawRoundOutline(m.getX() - 4, m.getY() - 4, (m.getWidth() - m.getX()) + 8, (m.getHeight() - m.getY()) + 8, 6, 1, new Color(255, 255, 255, 0), new Color(255, 255, 255, (int) m.editOpacityAnimation.getValue()));
				}
				
				if(m.isDragging()) {
					m.setX(mouseX + m.getDraggingX());
					m.setY(mouseY + m.getDraggingY());
				}
			}
		}

         super.drawScreen(mouseX, mouseY, partialTicks);
         
         if(clickEffects.size() > 0) {
             Iterator<ClickEffect> clickEffectIterator= clickEffects.iterator();
             while(clickEffectIterator.hasNext()){
                 ClickEffect clickEffect = clickEffectIterator.next();
                 clickEffect.draw();
                 if (clickEffect.canRemove()) clickEffectIterator.remove();
             }
         }
	}
	
    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
    	if(keyCode == 1) {
    		if(fromClickGUI) {
        		mc.displayGuiScreen(HackSoar.instance.guiManager.getClickGUI());
    		}else {
    			mc.displayGuiScreen(null);
    		}
    	}
    }
    
	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		
        ClickEffect clickEffect = new ClickEffect(mouseX, mouseY);
        clickEffects.add(clickEffect);
        
		for(Mod m : HackSoar.instance.modManager.getMods()) {
			if(m.isToggled() && m.getCategory().equals(ModCategory.HUD)) {
				
				boolean isInside = m.getName().equals("Scoreboard") ?  MouseUtils.isInside(mouseX, mouseY, ScoreboardMod.shadowX, ScoreboardMod.shadowY, ScoreboardMod.shadowWidth - ScoreboardMod.shadowX, ScoreboardMod.shadowHeight - ScoreboardMod.shadowY)
						: MouseUtils.isInside(mouseX, mouseY, m.getX(), m.getY(), m.getWidth() - m.getX(), m.getHeight() - m.getY()) && 
						HackSoar.instance.modManager.getMods().stream().filter(m2 -> m2.isToggled() && m2.getCategory().equals(ModCategory.HUD) && mouseX >= m2.getX() && mouseX <= m2.getWidth() && mouseY >= m2.getY() && mouseY <= m2.getHeight()).findFirst().get().equals(m);
				
				if(isInside) {
					m.setDragging(true);
					m.setDraggingX(m.getX() - mouseX);
					m.setDraggingY(m.getY() - mouseY);
				}
			}
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {  
		for(Mod m : HackSoar.instance.modManager.getMods()){
			m.setDragging(false);
		}
	}
	
	@Override
	public void onGuiClosed() {
		for(Mod m : HackSoar.instance.modManager.getMods()){
			m.setDragging(false);
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
