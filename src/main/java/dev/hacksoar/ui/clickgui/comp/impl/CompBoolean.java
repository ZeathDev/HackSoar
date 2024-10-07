package dev.hacksoar.ui.clickgui.comp.impl;


import dev.hacksoar.HackSoar;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.ui.clickgui.comp.Comp;
import dev.hacksoar.ui.clickgui.impl.FeatureCategory;
import dev.hacksoar.ui.clickgui.impl.features.CombatModules;
import dev.hacksoar.ui.clickgui.impl.features.MovementModules;
import dev.hacksoar.ui.clickgui.impl.features.RenderModules;
import dev.hacksoar.ui.clickgui.impl.features.UtiltyModules;
import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import dev.hacksoar.utils.color.ColorUtils;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.mouse.MouseUtils;
import dev.hacksoar.utils.render.GlUtils;
import dev.hacksoar.utils.render.RoundedUtils;

import java.awt.*;

public class CompBoolean extends Comp {

	private final SimpleAnimation animation = new SimpleAnimation(0.0F);
	private final SimpleAnimation animation2 = new SimpleAnimation(0.0F);
	
    public CompBoolean(double x, double y, FeatureCategory parent, Module mod, BoolValue setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.mod = mod;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
    	animation.setAnimation((boolean) setting.get() ? 1 : 0, 10);
    	animation2.setAnimation((boolean) setting.get() ? 255 : 0, 12);
    	
    	RoundedUtils.drawRound((float) (parent.getX() + x - 70), (float) (parent.getY() + y), 10, 10, 3, ColorUtils.getBackgroundColor(2));
    	
    	GlUtils.startScale((float) (parent.getX() + x - 70 + parent.getX() + x - 70 + 10) / 2, (float) (parent.getY() + y + parent.getY() + y + 10) / 2, animation.getValue());
    	RoundedUtils.drawRound((float) (parent.getX() + x - 70), (float) (parent.getY() + y), 10, 10, 3, ColorUtils.getClientColor(0, (int) animation2.getValue()));
        FontUtils.icon20.drawString("H", (float) (parent.getX() + x - 70), (float) (parent.getY() + y + 3), new Color(255, 255, 255).getRGB());
        GlUtils.stopScale();

        FontUtils.regular20.drawString(setting.getName(), (float) (parent.getX() + x - 55), (float) (parent.getY() + y + 2), ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float ad = 0;
        if(HackSoar.instance.guiManager.getgClickGUI().selectedCategory.equals(HackSoar.instance.guiManager.getgClickGUI().categoryManager.getCategoryByClass(FeatureCategory.class))) {
            ad = FeatureCategory.scrollVAnimation.getValue();
        }
        if(HackSoar.instance.guiManager.getgClickGUI().selectedCategory.equals(HackSoar.instance.guiManager.getgClickGUI().categoryManager.getCategoryByClass(CombatModules.class))) {
            ad = CombatModules.scrollVAnimation.getValue();
        }
        if(HackSoar.instance.guiManager.getgClickGUI().selectedCategory.equals(HackSoar.instance.guiManager.getgClickGUI().categoryManager.getCategoryByClass(MovementModules.class))) {
            ad = MovementModules.scrollVAnimation.getValue();
        }
        if(HackSoar.instance.guiManager.getgClickGUI().selectedCategory.equals(HackSoar.instance.guiManager.getgClickGUI().categoryManager.getCategoryByClass(RenderModules.class))) {
            ad = RenderModules.scrollVAnimation.getValue();
        }
        if(HackSoar.instance.guiManager.getgClickGUI().selectedCategory.equals(HackSoar.instance.guiManager.getgClickGUI().categoryManager.getCategoryByClass(UtiltyModules.class))) {
            ad = UtiltyModules.scrollVAnimation.getValue();
        }
        if(MouseUtils.isInside(mouseX, mouseY, parent.getX() + x - 70, parent.getY() + y + ad, 10, 10)) {
            if (mouseButton == 0) {
                setting.set(!(boolean)setting.get());
                parent.isHitd = true;
            } else if (mouseButton == 1) {
                setting.set(setting.getDefaultVal());
                parent.isHitd = true;
            }
        }
    }
}
