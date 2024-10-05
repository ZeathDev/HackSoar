package dev.hacksoar.ui.clickgui.comp.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.ui.clickgui.comp.Comp;
import dev.hacksoar.ui.clickgui.impl.FeatureCategory;
import dev.hacksoar.ui.clickgui.impl.features.CombatModules;
import dev.hacksoar.ui.clickgui.impl.features.MovementModules;
import dev.hacksoar.ui.clickgui.impl.features.RenderModules;
import dev.hacksoar.ui.clickgui.impl.features.UtiltyModules;
import dev.hacksoar.utils.color.ColorUtils;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.mouse.MouseUtils;
import dev.hacksoar.utils.render.RoundedUtils;

public class CompList extends Comp {

    public CompList(double x, double y, FeatureCategory parent, Module mod, ListValue setting) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.mod = mod;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);

        RoundedUtils.drawRound((float) (parent.getX() + x - 70), (float) (parent.getY() + y), (float) FontUtils.regular20.getStringWidth(setting.getName() + ": " + setting.get()) + 5, 11, 3, ColorUtils.getBackgroundColor(2));
        FontUtils.regular20.drawString(setting.getName() + ": " + setting.get(), (int)(parent.getX() + x - 68), (int)(parent.getY() + y + 2), ColorUtils.getFontColor(2).getRGB());
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
        if (MouseUtils.isInside(mouseX, mouseY, parent.getX() + x - 70, parent.getY() + y + ad, 70, 10)) {
            if (mouseButton == 0) {
                int max = ((ListValue) setting).getValues().length;
                if (parent.modeIndex + 1 >= max) {
                    parent.modeIndex = 0;
                } else {
                    parent.modeIndex++;
                }
                setting.set((((ListValue) setting).getValues())[parent.modeIndex]);
                parent.isHitd = true;
            } else if (mouseButton == 1) {
                int max = ((ListValue) setting).getValues().length;
                if (parent.modeIndex - 1 < 0) {
                    parent.modeIndex = max - 1;
                } else {
                    parent.modeIndex--;
                }
                setting.set((((ListValue) setting).getValues())[parent.modeIndex]);
                parent.isHitd = true;
            }
        }
    }
}
