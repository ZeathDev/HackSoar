package dev.hacksoar.ui.clickgui.impl;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.value.Value;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.FloatValue;
import dev.hacksoar.api.value.impl.IntValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.ui.clickgui.Category;
import dev.hacksoar.ui.clickgui.ClickGui;
import dev.hacksoar.ui.clickgui.comp.Comp;
import dev.hacksoar.ui.clickgui.comp.impl.CompBoolean;
import dev.hacksoar.ui.clickgui.comp.impl.CompFloat;
import dev.hacksoar.ui.clickgui.comp.impl.CompInt;
import dev.hacksoar.ui.clickgui.comp.impl.CompList;
import dev.hacksoar.utils.animation.normal.Animation;
import dev.hacksoar.utils.animation.normal.Direction;
import dev.hacksoar.utils.animation.normal.impl.EaseInOutQuad;
import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import dev.hacksoar.utils.color.ColorUtils;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.mouse.MouseUtils;
import dev.hacksoar.utils.render.GlUtils;
import dev.hacksoar.utils.render.RoundedUtils;
import dev.hacksoar.utils.render.StencilUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class FeatureCategory extends Category {
    public static double scrollY,scrollYV;
    public static SimpleAnimation scrollAnimation = new SimpleAnimation(0.0F);
    public static SimpleAnimation scrollVAnimation = new SimpleAnimation(0.0F);
    public static boolean openModSetting;
    private Module selectedMod;
    public static Animation openSettingAnimation;

    private boolean canToggle;
    public boolean isHitd = false;
    public static ArrayList<Comp> comps = new ArrayList<>();
    public int modeIndex, modIndex = 1;
    private ModuleCategory category;

    public FeatureCategory() {
        super("Feature");
    }

    public FeatureCategory(String name, ModuleCategory category) {
        super(name);
        this.category = category;
    }

    @Override
    public void initGui() {
        openModSetting = false;
        for(Module m : HackSoar.instance.moduleManager.getModules()) {
            m.selectTimer.reset();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int offset = 15;
        int tempModeIndex = 0;
        int valueIndex = 1;

        ClickGui clickGUI = HackSoar.instance.guiManager.getgClickGUI();
        GlUtils.startTranslate(openModSetting ? (float) -openSettingAnimation.getValue() : 0, 0);

        for(Module m : HackSoar.instance.moduleManager.getModules()) {
            if (category != null) {
                if (m.getModuleCategory() != category) {
                    continue;
                }
            }
            if(!clickGUI.searchMode || (StringUtils.containsIgnoreCase(m.moduleName, clickGUI.searchWord.getText()))) {
                RoundedUtils.drawRound(this.getX() + 95, this.getY() + offset + scrollAnimation.getValue(), 200, 26, 6, ColorUtils.getBackgroundColor(4));
                FontUtils.regular20.drawString(m.moduleName, this.getX() + 105, this.getY() + 10.5F + offset + scrollAnimation.getValue(), ColorUtils.getFontColor(2).getRGB());

                m.buttonAnimation.setAnimation(m.getToggled() ? 14 : 0, 12);
                m.buttonOpacityAnimation.setAnimation(m.getToggled() ? 255 : 0, 12);

                RoundedUtils.drawRound(this.getX() + 242, this.getY() + offset + scrollAnimation.getValue() + 7F, 28, 12.5F, 6, ColorUtils.getBackgroundColor(1));

                RoundedUtils.drawGradientRoundLR(this.getX() + 242, this.getY() + offset + scrollAnimation.getValue() + 7F, 28, 12.5F, 6, ColorUtils.getClientColor(0, (int) m.buttonOpacityAnimation.getValue()), ColorUtils.getClientColor(90, (int) m.buttonOpacityAnimation.getValue()));

                RoundedUtils.drawRound(this.getX() + 244 + m.buttonAnimation.getValue(), this.getY() + offset + 7F + scrollAnimation.getValue() + 1.3F, 10, 10, 5, ColorUtils.getBackgroundColor(4));

                if (m.getValues().size() > 0) {
                    FontUtils.icon20.drawString("A", this.getX() + 278, this.getY() + offset + 11F + scrollAnimation.getValue(), ColorUtils.getFontColor(2).getRGB());
                }

                offset+=35;
                tempModeIndex++;
            }

            if(clickGUI.close) {
                m.selectTimer.reset();
            }
        }

        modIndex = tempModeIndex;

        GlUtils.stopTranslate();

        scrollAnimation.setAnimation((float) scrollY, 16);

        if(openSettingAnimation != null && selectedMod != null) {
            valueIndex = 0;
            GlUtils.startTranslate(openModSetting ? (float) -openSettingAnimation.getValue() + 220 : 220, 0 + scrollVAnimation.getValue());

            for (Comp comp : comps) {
                // comp.drawScreen(mouseX, mouseY);
                if (comp.setting instanceof IntValue || comp.setting instanceof FloatValue) {
                    valueIndex+=2;
                }
                if (comp.setting instanceof BoolValue || comp.setting instanceof ListValue) {
                    valueIndex++;
                }
            }

            RoundedUtils.drawRound(this.getX() + 95, this.getY() + 10, 200, valueIndex <= 13 ? 210 : 210 + (valueIndex-13) * 18, 6, ColorUtils.getBackgroundColor(4));
            FontUtils.regular24.drawString(selectedMod.moduleName, this.getX() + 100, this.getY() + 19, ColorUtils.getFontColor(1).getRGB());

            for (Comp comp : comps) {
                comp.drawScreen(mouseX, mouseY);
            }


            if(openSettingAnimation.isDone(Direction.BACKWARDS)) {
                if(openModSetting) {
                    openModSetting = false;
                    comps.clear();
                }
            }

            GlUtils.stopTranslate();
        }

        canToggle = MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());

        final MouseUtils.Scroll scroll = MouseUtils.scroll();


        if(scroll != null) {
            if (!openModSetting) {
                switch (scroll) {
                    case DOWN:
                        if(scrollY > -((modIndex - 5) * 35)) {
                            scrollY -=20;
                        }

                        if(modIndex > 6) {
                            if(scrollY < -((modIndex - 6) * 35)) {
                                scrollY = -((modIndex - 6) * 35);
                            }
                        }
                        break;
                    case UP:
                        if(scrollY < -10) {
                            scrollY +=20;
                        } else {
                            if(modIndex > 6) {
                                scrollY = 0;
                            }
                        }
                        break;
                }
            } else {
                switch (scroll) {
                    case DOWN:
                        if(scrollYV > -((valueIndex - 6.5) * 35)) {
                            scrollYV -=20;
                        }

                        if(valueIndex > 13) {
                            if(scrollYV < -((valueIndex - 8) * 35)) {
                                scrollYV = -((valueIndex - 7.1) * 35);
                            }
                        }
                        break;
                    case UP:
                        if(scrollYV < -10) {
                            scrollYV +=20;
                        }else {
                            if(valueIndex > 13) {
                                scrollYV = 0;
                            }
                        }
                        break;
                }
            }

        }

        scrollVAnimation.setAnimation((float) scrollYV, 16);

        StencilUtils.uninitStencilBuffer();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int offset = 15;
        ClickGui clickGUI = HackSoar.instance.guiManager.getgClickGUI();

        for(Module m : HackSoar.instance.moduleManager.getModules()) {
            if (category != null) {
                if (m.getModuleCategory() != category) {
                    continue;
                }
            }
            if(!clickGUI.searchMode || (StringUtils.containsIgnoreCase(m.moduleName, clickGUI.searchWord.getText()))) {

                if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 270, this.getY() + offset + scrollAnimation.getValue(), 26, 26) && canToggle && !openModSetting) {
                    if (mouseButton == 0) {
                        scrollYV = 0;
                        int sOffset = 35;

                        comps.clear();

                        if (m.getValues().size() > 0) {
                            for (Value setting : m.getValues()) {
                                selectedMod = m;
                                openModSetting = true;

                                openSettingAnimation = new EaseInOutQuad(300, 220);

                                if (! (boolean) setting.getDisplayable().invoke()) {
                                    continue;
                                }

                                if (setting instanceof ListValue) {
                                    comps.add(new CompList(175, sOffset, this, selectedMod,(ListValue) setting));
                                    sOffset += 15;
                                }
                                if (setting instanceof BoolValue) {
                                    comps.add(new CompBoolean(175, sOffset, this, selectedMod, (BoolValue) setting));
                                    sOffset += 15;
                                }
                                if (setting instanceof FloatValue) {
                                    comps.add(new CompFloat(175, sOffset, this, selectedMod, (FloatValue) setting));
                                    sOffset += 25;
                                }
                                if (setting instanceof IntValue) {
                                    comps.add(new CompInt(175, sOffset, this, selectedMod, (IntValue) setting));
                                    sOffset += 25;
                                }
                            }
                        }
                    }
                }

                if(MouseUtils.isInside(mouseX, mouseY, this.getX() + 95, this.getY() + offset + scrollAnimation.getValue(), 175, 26) && canToggle) {
                    if(mouseButton == 0 && !openModSetting) {
                        m.toggle();
                    }
                }
                offset+=35;
            }
        }

        if(openModSetting) {
            for (Comp comp : comps) {
                comp.mouseClicked(mouseX, mouseY, mouseButton);
            }

            if (!openModSetting || selectedMod == null || !isHitd || !canToggle) {
                // Logger.log("ret");
                return;
            }

            int sOffset = 35;

            comps.clear();

            if (selectedMod.getValues().size() > 0) {
                for (Value setting : selectedMod.getValues()) {
                    if (!(boolean) setting.getDisplayable().invoke()) {
                        continue;
                    }

                    if (setting instanceof ListValue) {
                        comps.add(new CompList(175, sOffset, this, selectedMod, (ListValue) setting));
                        sOffset += 15;
                    }
                    if (setting instanceof BoolValue) {
                        comps.add(new CompBoolean(175, sOffset, this, selectedMod, (BoolValue) setting));
                        sOffset += 15;
                    }
                    if (setting instanceof FloatValue) {
                        comps.add(new CompFloat(175, sOffset, this, selectedMod, (FloatValue) setting));
                        sOffset += 25;
                    }
                    if (setting instanceof IntValue) {
                        comps.add(new CompInt(175, sOffset, this, selectedMod, (IntValue) setting));
                        sOffset += 25;
                    }
                }
            }
            isHitd = false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Comp comp : comps) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (Comp comp : comps) {
            comp.keyTyped(typedChar, keyCode);
        }
    }
}