package dev.hacksoar.pvp.management.keybinds;

import dev.hacksoar.HackSoar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class KeyBindManager {

	public KeyBinding STOPWATCH = new KeyBinding("Stopwatch", Keyboard.KEY_P, "HackSoar");
	public KeyBinding QUICKPLAY = new KeyBinding("Quick Play", Keyboard.KEY_V, "HackSoar");
	public KeyBinding ZOOM = new KeyBinding("Zoom", Keyboard.KEY_C, "HackSoar");
	public KeyBinding FREELOOK = new KeyBinding("Freelook", Keyboard.KEY_Z, "HackSoar");
	public KeyBinding SCREENSHOT_VIEWER = new KeyBinding("Screenshot Viewer", Keyboard.KEY_M, "HackSoar");
	public KeyBinding CLICKGUI = new KeyBinding("Click GUI", Keyboard.KEY_RSHIFT, "HackSoar");
	public KeyBinding HACKCLICKGUI = new KeyBinding("Hack Click GUI", Keyboard.KEY_RSHIFT, "HackSoar");
	public KeyBinding EDITHUD = new KeyBinding("Edit HUD", Keyboard.KEY_H, "HackSoar");
	public KeyBinding TAPLOOK = new KeyBinding("Taplook", Keyboard.KEY_L, "HackSoar");
	
	public KeyBindManager() {
		try {
			this.unregisterKeybind((KeyBinding) GameSettings.class.getField("ofKeyBindZoom").get(Minecraft.getMinecraft().gameSettings));
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// Don't give crackers clues...
			if (HackSoar.instance.DEVELOPMENT_SWITCH)
				e.printStackTrace();
		}
		
		this.registerKeyBind(CLICKGUI);
		this.registerKeyBind(HACKCLICKGUI);
		this.registerKeyBind(EDITHUD);
		this.registerKeyBind(SCREENSHOT_VIEWER);
		this.registerKeyBind(STOPWATCH);
		this.registerKeyBind(QUICKPLAY);
		this.registerKeyBind(FREELOOK);
		this.registerKeyBind(TAPLOOK);
		this.registerKeyBind(ZOOM);
	}
	
    public void registerKeyBind(KeyBinding key) {
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.add(Minecraft.getMinecraft().gameSettings.keyBindings, key);
    }

    public void unregisterKeybind(KeyBinding key) {
        if (Arrays.asList(Minecraft.getMinecraft().gameSettings.keyBindings).contains(key)) {
            Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.remove(Minecraft.getMinecraft().gameSettings.keyBindings, Arrays.asList(Minecraft.getMinecraft().gameSettings.keyBindings).indexOf(key));
            key.setKeyCode(0);
        }
    }
}
