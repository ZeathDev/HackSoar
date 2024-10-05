package dev.hacksoar.utils.font;

import dev.hacksoar.HackSoar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static int scale;
    private static int prevScale;

    public static FontDrawer test18;
    private static Font font18;

    public static void init() {
        Map<String, Font> locationMap = new HashMap<>();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        scale = sr.getScaleFactor();

        if(scale != prevScale) {
            prevScale = scale;

            FontManager.font18 = getFont(locationMap, "sarasa-gothic-sc-regular.ttf", 36);
            FontManager.test18 = new FontDrawer(FontManager.font18, true, false);
        }
    }

    private static Font getFont(Map<String, Font> locationMap, String location, float size) {
        Font font;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        size = size * ((float) sr.getScaleFactor() / 2);

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("soar/fonts/" + location)).getInputStream();
                locationMap.put(location, font = Font.createFont(0, is));
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            // Don't give crackers clues...
            if (HackSoar.instance.DEVELOPMENT_SWITCH)
                e.printStackTrace();
            font = new Font("default", Font.PLAIN, +10);
        }
        return font;
    }
}
