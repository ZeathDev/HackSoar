package dev.hacksoar.pvp.management.mods.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.pvp.management.image.Image;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.mods.ModCategory;
import dev.hacksoar.utils.GlUtils;
import dev.hacksoar.utils.render.RenderUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageDisplayMod extends Mod {

    private ResourceLocation image;
    private int imageWidth, imageHeight;
    private Image prevImage;
    
	public ImageDisplayMod() {
		super("Image Display", "Display image", ModCategory.HUD);
	}
	
	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<String>();
		
		if(HackSoar.instance.imageManager.getImages().isEmpty()) {
			options.add("None");
		}else {
			for(Image i : HackSoar.instance.imageManager.getImages()) {
				options.add(i.getName());
			}
		}
		
		this.addModeSetting("Image", this, "None", options);
		this.addSliderSetting("Opacity", this, 1.0, 0.1, 1.0, false);
		this.addSliderSetting("Scale", this, 1.0, 0.1, 3.0, false);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
		String mode = HackSoar.instance.settingsManager.getSettingByName(this, "Image").getValString();
		float opacity = HackSoar.instance.settingsManager.getSettingByName(this, "Opacity").getValFloat();
		float scale = HackSoar.instance.settingsManager.getSettingByName(this, "Scale").getValFloat();
		
		if(mode.equals("None")) {
			return;
		}
		
		HackSoar.instance.imageManager.setCurrentImage(HackSoar.instance.imageManager.getImageByName(mode));
		
		if(prevImage != HackSoar.instance.imageManager.getCurrentImage()) {
			prevImage = HackSoar.instance.imageManager.getCurrentImage();
	        try {
	            BufferedImage t = ImageIO.read(HackSoar.instance.imageManager.getCurrentImage().getFile());
	            DynamicTexture nibt = new DynamicTexture(t);

	            imageWidth = t.getWidth();
	            imageHeight = t.getHeight();
	            
	            this.image = mc.getTextureManager().getDynamicTextureLocation("Image", nibt);
	        } catch (Throwable e) {
				// Don't give crackers clues...
				if (HackSoar.instance.DEVELOPMENT_SWITCH)
					e.printStackTrace();
	        }
		}
        
        if(image != null) {
        	GlUtils.startScale(this.getX(), this.getY(), scale);
        	RenderUtils.drawImage(image, this.getX(), this.getY(), imageWidth / 2, imageHeight / 2, opacity);
        	GlUtils.stopScale();
        }
        
        this.setWidth((int) ((imageWidth / 2) * (scale)));
        this.setHeight((int) ((imageHeight / 2) * (scale)));
	}
}
