package dev.hacksoar.pvp.clickgui.comp.impl;

import dev.hacksoar.pvp.clickgui.category.impl.FeatureCategory;
import dev.hacksoar.pvp.clickgui.comp.Comp;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.settings.Setting;
import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import dev.hacksoar.utils.color.ColorUtils;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.mouse.MouseUtils;
import dev.hacksoar.utils.render.GlUtils;
import dev.hacksoar.utils.render.RoundedUtils;

import java.awt.*;

public class CompCheckBox extends Comp {

	private final SimpleAnimation animation = new SimpleAnimation(0.0F);
	private final SimpleAnimation animation2 = new SimpleAnimation(0.0F);
	
    public CompCheckBox(float x, float y, FeatureCategory parent, Mod mod, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.mod = mod;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
    	
    	animation.setAnimation(setting.getValBoolean() ? 1 : 0, 10);
    	animation2.setAnimation(setting.getValBoolean() ? 255 : 0, 12);
    	
    	RoundedUtils.drawRound(parent.getX() + x - 70, parent.getY() + y, 10, 10, 3, ColorUtils.getBackgroundColor(2));
    	
    	GlUtils.startScale((parent.getX() + x - 70 + parent.getX() + x - 70 + 10) / 2, (parent.getY() + y + parent.getY() + y + 10) / 2, animation.getValue());
    	RoundedUtils.drawRound(parent.getX() + x - 70, parent.getY() + y, 10, 10, 3, ColorUtils.getClientColor(0, (int) animation2.getValue()));
    	FontUtils.icon20.drawString("H", (parent.getX() + x - 70), (parent.getY() + y + 3), new Color(255, 255, 255).getRGB());
    	GlUtils.stopScale();
    	
        FontUtils.regular20.drawString(setting.getName(), (int)(parent.getX() + x - 55), (parent.getY() + y + 2), ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtils.isInside(mouseX, mouseY, parent.getX() + x - 70, parent.getY() + y, 10, 10) && mouseButton == 0) {
            setting.setValBoolean(!setting.getValBoolean());
        }
    }

}
