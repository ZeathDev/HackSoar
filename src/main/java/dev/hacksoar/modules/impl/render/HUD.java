package dev.hacksoar.modules.impl.render;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.color.ColorUtils;
import dev.hacksoar.utils.font.FontDrawer;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

@ModuleTag
public class HUD extends Module {
    private final BoolValue watermark = new BoolValue("Water mark", true);
    private final BoolValue modulesList = new BoolValue("Modules list", true);
    private final BoolValue modulesListBackground = new BoolValue("Modules list background", true, modulesList::get);

    public HUD() {
        super("HUD", "display modules...", ModuleCategory.Render, true);
    }

    @EventTarget
    private void onRender2D(EventRender2D event) {
        if (mc.gameSettings.showDebugInfo) {
            return;
        }

        ScaledResolution sr = new ScaledResolution(mc);
        FontDrawer f18 = FontUtils.regular_bold18;

        if (watermark.get()) {
            f18.drawStringWithShadow(HackSoar.instance.getName() + " v" + HackSoar.instance.getVersion(), 2, 2, -1);
        }

        if (modulesList.get()) {
            final ArrayList<Module> modules = new ArrayList<>();

            for (Module module : HackSoar.instance.moduleManager.getModules()) {
                if (module.toggled) {
                    modules.add(module);
                }
            }

            if (modules.isEmpty()) return;

            modules.sort((o1, o2) -> f18.getStringWidth(o2.moduleName.toLowerCase()) - f18.getStringWidth(o1.moduleName.toLowerCase()));

            float x;
            float y = 2;
            for (int i = 0; i < modules.size(); ++i) {
                int color = ColorUtils.getClientColor(i * 10).getRGB();
                final String name = modules.get(i).moduleName.toLowerCase();
                x = sr.getScaledWidth() - f18.getStringWidth(name) - 2;

                if (modulesListBackground.get()) {
                    RenderUtils.drawRect(x - 2f, y - 3, f18.getStringWidth(name) + 4, 11f, new Color(0, 0, 0, 150).getRGB());
                }
                f18.drawStringWithShadow(name, x, y, color);

                y += 11;
            }
        }
    }
}
