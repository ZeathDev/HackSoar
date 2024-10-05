package dev.hacksoar.utils;

import dev.hacksoar.HackSoar;
import dev.hacksoar.pvp.management.mods.impl.FullbrightMod;
import dev.hacksoar.ui.notification.Notification;
import dev.hacksoar.ui.notification.NotificationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.lang.reflect.Field;

public class ClientUtils {
	
    public static Field gameSettings_ofFastRender;
	
	private static Notification notification = new Notification();
	
	public static boolean loadedMinimap = false;
	
	public static void showNotification(String title, String message) {
		notification.setNotification(title, message);
		NotificationManager.show(notification);
	}
	
    public static File getModsDir() {
    	return new File(Minecraft.getMinecraft().mcDataDir, "mods");
    }
    
    public static boolean isFullbright() {
        MinecraftServer server = MinecraftServer.getServer();

        if (server != null && server.isCallingFromMinecraftThread()) {
            return false;
        }

        return HackSoar.instance.modManager.getModByClass(FullbrightMod.class).isToggled();
    }

    static {
        try {
            Class.forName("Config");

            gameSettings_ofFastRender = GameSettings.class.getDeclaredField("ofFastRender");
            gameSettings_ofFastRender.setAccessible(true);
        } catch (ClassNotFoundException ignore) {
        } catch (NoSuchFieldException e) {}
    }

	public static void shutdown() {
		Minecraft.getMinecraft().shutdown();
	}
}