package dev.hacksoar.pvp.clickgui.comp;

import dev.hacksoar.pvp.clickgui.category.impl.FeatureCategory;
import dev.hacksoar.pvp.management.mods.Mod;
import dev.hacksoar.pvp.management.settings.Setting;

public class Comp {

    public float x, y, width, height;
    public FeatureCategory parent;
    public Mod mod;
    public Setting setting;

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY, int state) {}

    public void drawScreen(int mouseX, int mouseY) {}

    public void keyTyped(char typedChar, int keyCode) {}

}
