package dev.hacksoar.ui.clickgui.comp.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.value.impl.FloatValue;
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
import dev.hacksoar.utils.render.RoundedUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CompFloat extends Comp {

    private boolean dragging = false, rest = false;
    private double renderWidth;
    private double renderWidth2;
    private SimpleAnimation animation = new SimpleAnimation(0.0F);
    
    public CompFloat(double x, double y, FeatureCategory parent, Module mod, FloatValue setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.mod = mod;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);

        double min = ((FloatValue)setting).getMinimum();
        double max = ((FloatValue)setting).getMaximum();
        double l = 90;

        renderWidth = (l) * (((FloatValue) setting).get() - min) / (max - min);
        renderWidth2 = (l) * (((FloatValue)setting).getMaximum() - min) / (max - min);

        animation.setAnimation((float) renderWidth, 14);
        
        double diff = Math.min(l, Math.max(0, mouseX - (parent.getX() + x - 70)));
        
        if (dragging) {
            if (rest) {
                setting.set(setting.getDefaultVal());
            } else {
                if (diff == 0) {
                    setting.set(((FloatValue)setting).getMinimum());
                } else {
                    double newValue = roundToPlace(((diff / l) * (max - min) + min), 2);
                    ((FloatValue) setting).set(newValue);
                }
            }
        }
        
        RoundedUtils.drawRound((float) (parent.getX() + x - 70), (float) (parent.getY() + y + 13), (float) (renderWidth2), 6, 3, ColorUtils.getBackgroundColor(2));
        RoundedUtils.drawGradientRoundLR((float) (parent.getX() + x - 70), (float) (parent.getY() + y + 13), animation.getValue(), 6, 3, ColorUtils.getClientColor(0), ColorUtils.getClientColor(20));
        RoundedUtils.drawRound((float) (parent.getX() + x) + animation.getValue() - 72, (float) (parent.getY() + y + 10), 5, 12, 2, ColorUtils.getBackgroundColor(1));

        FontUtils.regular20.drawString(setting.getName() + ": " + setting.get(),(int)(parent.getX() + x - 70),(int)(parent.getY() + y), ColorUtils.getFontColor(2).getRGB());
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
        if (MouseUtils.isInside(mouseX, mouseY, parent.getX() + x - 70, parent.getY() + y + 10 + ad,renderWidth2 + 3, 10)) {
            if (mouseButton == 0) {
                dragging = true;
            } else if (mouseButton == 1) {
                dragging = true;
                rest = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
        rest = false;
    }

    private double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
