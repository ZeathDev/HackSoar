package dev.hacksoar.ui.minimap;

import dev.hacksoar.ui.minimap.interfaces.InterfaceHandler;
import net.minecraft.client.Minecraft;

import java.io.IOException;

public class XaeroMinimap
{
    public static XaeroMinimap instance;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public void load() throws IOException {
        InterfaceHandler.loadPresets();
        InterfaceHandler.load();
    }
}
