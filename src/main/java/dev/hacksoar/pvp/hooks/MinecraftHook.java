package dev.hacksoar.pvp.hooks;

import dev.hacksoar.HackSoar;
import dev.hacksoar.modules.impl.utilty.MemoryFix;
import dev.hacksoar.pvp.management.mods.impl.BorderlessFullscreenMod;
import net.minecraft.client.Minecraft;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static net.minecraft.client.Minecraft.displayFixCancel;

public class MinecraftHook {

    
    public static void displayFix(boolean fullscreen, int displayWidth, int displayHeight) throws LWJGLException {
        Display.setFullscreen(false);
        if (fullscreen) {
            if (HackSoar.instance.modManager != null && HackSoar.instance.modManager.getModByClass(BorderlessFullscreenMod.class).isToggled()) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            } else {
                Display.setFullscreen(true);
                DisplayMode displaymode = Display.getDisplayMode();
                Minecraft.getMinecraft().displayWidth = Math.max(1, displaymode.getWidth());
                Minecraft.getMinecraft().displayHeight = Math.max(1, displaymode.getHeight());
            }
        } else {
            if (HackSoar.instance.modManager != null && HackSoar.instance.modManager.getModByClass(BorderlessFullscreenMod.class).isToggled()) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
            } else {
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        }

        Display.setResizable(false);
        Display.setResizable(true);

        displayFixCancel = true;
    }
    
    public static void fullScreenFix(boolean fullscreen, int displayWidth, int displayHeight) throws LWJGLException {
        if (HackSoar.instance.modManager != null && HackSoar.instance.modManager.getModByClass(BorderlessFullscreenMod.class).isToggled()) {
            if (fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                Display.setLocation(0, 0);
                Display.setFullscreen(false);
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        } else {
            Display.setFullscreen(fullscreen);
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        }

        Display.setResizable(false);
        Display.setResizable(true);
    }

    public static void memoryFix() {
        if (!HackSoar.instance.moduleManager.getModule("MemoryFix").toggled || !MemoryFix.fastLoadFix.get()) {
            System.gc();
        }
    }
}
