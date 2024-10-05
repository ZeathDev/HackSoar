package dev.hacksoar.pvp.clickgui.comp.impl;

import dev.hacksoar.pvp.clickgui.category.impl.FeatureCategory;
import dev.hacksoar.pvp.clickgui.comp.Comp;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.settings.Setting;
import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import dev.hacksoar.utils.color.ColorUtils;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.mouse.MouseUtils;
import dev.hacksoar.utils.render.RoundedUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CompSlider extends Comp {

    private boolean dragging = false;
    private double renderWidth;
    private double renderWidth2;
    private SimpleAnimation animation = new SimpleAnimation(0.0F);
    
    public CompSlider(float x, float y, FeatureCategory parent, Mod mod, Setting setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.mod = mod;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);

        double min = setting.getMin();
        double max = setting.getMax();
        double l = 90;

        renderWidth = (l) * (setting.getValDouble() - min) / (max - min);
        renderWidth2 = (l) * (setting.getMax() - min) / (max - min);

        animation.setAnimation((float) renderWidth, 14);
        
        double diff = Math.min(l, Math.max(0, mouseX - (parent.getX() + x - 70)));
        
        if (dragging) {
            if (diff == 0) {
                setting.setValDouble(setting.getMin());
            }
            else {
                double newValue = roundToPlace(((diff / l) * (max - min) + min), 2);
                setting.setValDouble(newValue);
            }
        }
        
        RoundedUtils.drawRound((float) (parent.getX() + x - 70), (float) (parent.getY() + y + 13), (float) (renderWidth2), 6, 3, ColorUtils.getBackgroundColor(2));
        RoundedUtils.drawGradientRoundLR((float) (parent.getX() + x - 70), (float) (parent.getY() + y + 13), animation.getValue(), 6, 3, ColorUtils.getClientColor(0), ColorUtils.getClientColor(20));
        RoundedUtils.drawRound((float) (parent.getX() + x) + animation.getValue() - 72, (float) (parent.getY() + y + 10), 5, 12, 2, ColorUtils.getBackgroundColor(1));
        
        FontUtils.regular20.drawString(setting.getName() + ": " + setting.getValDouble(),(int)(parent.getX() + x - 70),(int)(parent.getY() + y), ColorUtils.getFontColor(2).getRGB());
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtils.isInside(mouseX, mouseY, parent.getX() + x - 70, parent.getY() + y + 10,renderWidth2 + 3, 10) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
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
