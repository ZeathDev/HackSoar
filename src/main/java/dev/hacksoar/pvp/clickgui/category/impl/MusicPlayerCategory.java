package dev.hacksoar.pvp.clickgui.category.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.pvp.clickgui.ClickGUI;
import dev.hacksoar.pvp.clickgui.category.Category;
import dev.hacksoar.pvp.clickgui.category.setting.SettingSlider;
import dev.hacksoar.pvp.management.mods.impl.ClientMod;
import dev.hacksoar.pvp.management.music.Music;
import dev.hacksoar.pvp.management.settings.Setting;
import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import dev.hacksoar.utils.color.ColorUtils;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.mouse.MouseUtils;
import dev.hacksoar.utils.render.RoundedUtils;
import javafx.scene.media.MediaPlayer.Status;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayerCategory extends Category {

    private SimpleAnimation scrollAnimation = new SimpleAnimation(0.0F);
	private SettingSlider volumeSlider = new SettingSlider();
	private boolean canToggle;
	
	public MusicPlayerCategory() {
		super("Music Player");
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	int offsetY = 15;
    	Setting volumeSetting = HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Volume");
    	boolean isRandom = HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Random").getValBoolean();
    	boolean isLoop = HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Loop").getValBoolean();
        ClickGUI clickGUI = HackSoar.instance.guiManager.getClickGUI();
        
    	HackSoar.instance.musicManager.loadMusic();
    	
    	for(Music m : HackSoar.instance.musicManager.getMusics()) {
    		
    		String musicName = m.getName();
    		int MAX_CHAR = 34;
    		int maxLength = (musicName.length() < MAX_CHAR) ? musicName.length() : MAX_CHAR;
			musicName = musicName.substring(0, maxLength);
			
			if(clickGUI.searchMode ? StringUtils.containsIgnoreCase(m.getName(), clickGUI.searchWord.getText()) : true) {
	    		RoundedUtils.drawRound((float) this.getX() + 95, (float) (this.getY() + offsetY + scrollAnimation.getValue()), 200, 26, 8, ColorUtils.getBackgroundColor(4));
	    		FontUtils.regular20.drawString(musicName, this.getX() + 105, this.getY() + offsetY + 11 + scrollAnimation.getValue(), ColorUtils.getFontColor(2).getRGB());
	    		offsetY+= 35;
			}
    	}
    	
    	RoundedUtils.drawRound((float) this.getX() + 80, (float) this.getY() + 200, 225, 30 + 0.5F, 6, ColorUtils.getBackgroundColor(1));
    	RoundedUtils.drawRound((float) this.getX() + 80, (float) this.getY() + 200, 228, 30F, 0, ColorUtils.getBackgroundColor(1));
    	
    	RoundedUtils.drawRound((float) (this.getX() + (this.getX() + this.getWidth())) / 2 + 33, (float) this.getY() + 205, 20, 20, 10, ColorUtils.getBackgroundColor(4));
    	
    	String icon = "B";
    	
    	if(HackSoar.instance.musicManager.getCurrentMusic() != null && HackSoar.instance.musicManager.getCurrentMusic().mediaPlayer != null) {
			if(HackSoar.instance.musicManager.getCurrentMusic().mediaPlayer.getStatus().equals(Status.PLAYING)) {
				icon = "C";
				HackSoar.instance.musicManager.getCurrentMusic().setVolume();
			}else {
				icon = "B";
			}
    	}
    	
		FontUtils.icon24.drawString(icon, (this.getX() + (this.getX() + this.getWidth())) / 2 + (icon.equals("B") ? 38 : 37), this.getY() + 212, ColorUtils.getFontColor(2).getRGB());
		
    	RoundedUtils.drawRound((float) (this.getX() + (this.getX() + this.getWidth())) / 2 + 63, (float) this.getY() + 205, 20, 20, 10, ColorUtils.getBackgroundColor(4));
    	FontUtils.icon24.drawString("G", (this.getX() + (this.getX() + this.getWidth())) / 2 + 68, this.getY() + 212, isRandom ? ColorUtils.getClientColor(0).getRGB() : ColorUtils.getFontColor(2).getRGB());
    	
    	RoundedUtils.drawRound((float) (this.getX() + (this.getX() + this.getWidth())) / 2 + 93, (float) this.getY() + 205, 20, 20, 10, ColorUtils.getBackgroundColor(4));
    	FontUtils.icon24.drawString("F", (this.getX() + (this.getX() + this.getWidth())) / 2 + 97, this.getY() + 212, isLoop ? ColorUtils.getClientColor(0).getRGB() : ColorUtils.getFontColor(2).getRGB());
    	
    	String volumeIcon = "";
    	
    	if(volumeSetting.getValDouble() < 0.5 && volumeSetting.getValDouble() != 0.0) {
    		volumeIcon = "J";
    	}else if(volumeSetting.getValDouble() > 0.5) {
    		volumeIcon = "K";
    	}else if(volumeSetting.getValDouble() == 0.0) {
    		volumeIcon = "I";
    	}
    	
		FontUtils.icon24.drawString(volumeIcon, this.getX() + 95, (this.getY() + this.getHeight()) - 18, ColorUtils.getFontColor(2).getRGB());
    	
    	RoundedUtils.drawRound((float) (this.getX() + (this.getX() + this.getWidth())) / 2 + 123, (float) this.getY() + 205, 20, 20, 10, ColorUtils.getBackgroundColor(4));
    	FontUtils.icon24.drawString("L", (this.getX() + (this.getX() + this.getWidth())) / 2 + 127, this.getY() + 212, ColorUtils.getFontColor(2).getRGB());
    	
        final MouseUtils.Scroll scroll = MouseUtils.scroll();

        if(scroll != null && HackSoar.instance.musicManager.getMusics().size() > 5) {
        	switch (scroll) {
        	case DOWN:
        		if(HackSoar.instance.musicManager.getScrollY() > -((HackSoar.instance.musicManager.getMusics().size() - 5.5) * 35)) {
        			HackSoar.instance.musicManager.setScrollY(HackSoar.instance.musicManager.getScrollY() - 20);
        		}
        		
        		if(HackSoar.instance.musicManager.getScrollY() < -((HackSoar.instance.musicManager.getMusics().size() - 6) * 35)) {
        			HackSoar.instance.musicManager.setScrollY(-((HackSoar.instance.musicManager.getMusics().size() - 5.2) * 35));
        		}
        		break;
            case UP:
        		if(HackSoar.instance.musicManager.getScrollY() < -10) {
        			HackSoar.instance.musicManager.setScrollY(HackSoar.instance.musicManager.getScrollY() + 20);
        		}else {
            		if(HackSoar.instance.musicManager.getMusics().size() > 5) {
            			HackSoar.instance.musicManager.setScrollY(0);
            		}
        		}
        		break;
        	}
        }
        
        scrollAnimation.setAnimation((float) HackSoar.instance.musicManager.getScrollY(), 16);
        
    	volumeSlider.setPosition(this.getX() + 188, (this.getY() + this.getHeight()) - 30, 55, volumeSetting);
    	volumeSlider.drawScreen(mouseX, mouseY);
    	
    	if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 80, this.getY(), 220, 199)) {
    		canToggle = true;
    	}else {
    		canToggle = false;
    	}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
    	int offsetY = 15;
        ClickGUI clickGUI = HackSoar.instance.guiManager.getClickGUI();
        
    	for(Music m : HackSoar.instance.musicManager.getMusics()) {
			if(clickGUI.searchMode ? StringUtils.containsIgnoreCase(m.getName(), clickGUI.searchWord.getText()) : true) {
	    		if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 95, this.getY() + offsetY + scrollAnimation.getValue(),  200, 26) && mouseButton == 0 && canToggle) {

					m.playAsyncMusic();
					
					if(HackSoar.instance.musicManager.getCurrentMusic() == null) {
						HackSoar.instance.musicManager.setCurrentMusic(m);
					}
					
					if(HackSoar.instance.musicManager.getCurrentMusic() != m || HackSoar.instance.musicManager.getCurrentMusic().mediaPlayer != null) {
						HackSoar.instance.musicManager.getCurrentMusic().mediaPlayer.stop();
						HackSoar.instance.musicManager.setCurrentMusic(m);
					}
	    		}
	        	offsetY+=35;
			}
    	}
    	
		if(HackSoar.instance.musicManager.getCurrentMusic() != null) {
			
			if(MouseUtils.isInside(mouseX, mouseY, (this.getX() + (this.getX() + this.getWidth())) / 2 + 33, this.getY() + 205, 20, 20) && mouseButton == 0) {
				if(HackSoar.instance.musicManager.getCurrentMusic().mediaPlayer.getStatus().equals(Status.PLAYING)) {
					HackSoar.instance.musicManager.getCurrentMusic().mediaPlayer.pause();
				}else {
					HackSoar.instance.musicManager.getCurrentMusic().mediaPlayer.play();
				}

			}
		}
		
		if(MouseUtils.isInside(mouseX, mouseY, (this.getX() + (this.getX() + this.getWidth())) / 2 + 63, this.getY() + 205, 20, 20) && mouseButton == 0) {
			HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Random").setValBoolean(!HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Random").getValBoolean());
			HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Loop").setValBoolean(false);
		}
		
		if(MouseUtils.isInside(mouseX, mouseY, (this.getX() + (this.getX() + this.getWidth())) / 2 + 93, this.getY() + 205, 20, 20) && mouseButton == 0) {
			HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Loop").setValBoolean(!HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Loop").getValBoolean());
			HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Random").setValBoolean(false);
		}
		
		if(MouseUtils.isInside(mouseX, mouseY, (this.getX() + (this.getX() + this.getWidth())) / 2 + 123, this.getY() + 205,  20, 20)) {
			try {
				Desktop.getDesktop().open(new File(mc.mcDataDir, "soar/music"));
			} catch (IOException e) {
				// Don't give crackers clues...
				if (HackSoar.instance.DEVELOPMENT_SWITCH)
					e.printStackTrace();
			}
		}
		
		volumeSlider.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		volumeSlider.mouseReleased(mouseX, mouseY, mouseButton);
	}
}
