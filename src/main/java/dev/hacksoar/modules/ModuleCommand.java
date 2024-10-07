package dev.hacksoar.modules;

import dev.hacksoar.HackSoar;
import dev.hacksoar.utils.irc.ServerUtils;
import dev.hacksoar.utils.player.PlayerUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ModuleCommand {
    @Getter
    private static final String prefix = ".";
    private static final ArrayList<String> commands = new ArrayList<>();

    public ModuleCommand() {
        commands.add("i");
        commands.add("SwitchChat");
        commands.add("Say");
        commands.add("Bind");
    }

    public static void runCommand(String command) {
        String preCommand = command.contains(" ") ? command.substring(0,command.indexOf(" ")) : command;
        try {
            switch (preCommand.toLowerCase()) {
                case "i": {
                    if (HackSoar.instance.moduleManager.getModule("IRC").getToggled()) {
                        ServerUtils.sendMessage(command.substring(2));
                    } else {
                        PlayerUtils.tellPlayer("IRC module is down, you cant send any messages");
                    }
                    break;
                }
                case "switchchat": {
                    if (!HackSoar.instance.moduleManager.getModule("IRC").getToggled()) {
                        PlayerUtils.tellPlayer("IRC module is down, you cant switch irc chat.");
                        break;
                    } else {
                        GuiScreen.setStillIrc(!GuiScreen.isStillIrc());
                        PlayerUtils.tellPlayer("Chat switch to " + (GuiScreen.isStillIrc() ? "IRC" : "World"));
                    }
                    break;
                }
                case "say": {
                    if (command.equalsIgnoreCase("say")) {
                        Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(".say");
                        Minecraft.getMinecraft().thePlayer.sendChatMessage(".say");
                    } else {
                        Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(command.substring(4));
                        Minecraft.getMinecraft().thePlayer.sendChatMessage(command.substring(4));
                    }
                    break;
                }
                case "bind": {
                    String pre = command.substring(5);
                    if (!pre.contains(" ") || pre.equalsIgnoreCase("bind ")) {
                        PlayerUtils.tellPlayer("Bind command error, please input again.");
                        break;
                    }
                    String name = pre.substring(0,pre.indexOf(" "));
                    Module targetModule = null;
                    for (Module module : HackSoar.instance.moduleManager.getModules()) {
                        if (module.getModuleName().equalsIgnoreCase(name)) {
                            targetModule = module;
                        }
                    }
                    if (targetModule == null) {
                        PlayerUtils.tellPlayer("Bind command error, no target module.");
                        break;
                    }
                    String targetKey = pre.substring(pre.indexOf(" ") + 1);
                    int key = keyConvert(targetKey);
                    HackSoar.instance.moduleManager.setKeybind(targetModule,key);
                    if (key == 0) {
                        PlayerUtils.tellPlayer("Bind " + targetModule.getModuleName() + " to NONE.");
                    } else {
                        PlayerUtils.tellPlayer("Bind " + targetModule.getModuleName() + " to " + targetKey.toUpperCase() + ".");
                    }
                    // PlayerUtils.tellPlayer("pre: " + pre + " name: " + name + " targetKey: " + targetKey + " key: " + key);
                    break;
                }
                default: {
                    /*
                      Don't forget to code break~
                     */
                    PlayerUtils.tellPlayer("Unknown command, What are you saying?");
                    break;
                }
            }
        } catch (Exception exception) {
            // Don't give crackers clues...
            if (HackSoar.instance.DEVELOPMENT_SWITCH)
                exception.printStackTrace();
        }
    }

    /**
     * Code from ChatGPT
     */
    public static int keyConvert(String key) {
        switch (key.toUpperCase()) {
            case "A":
                return Keyboard.KEY_A;
            case "B":
                return Keyboard.KEY_B;
            case "C":
                return Keyboard.KEY_C;
            case "D":
                return Keyboard.KEY_D;
            case "E":
                return Keyboard.KEY_E;
            case "F":
                return Keyboard.KEY_F;
            case "G":
                return Keyboard.KEY_G;
            case "H":
                return Keyboard.KEY_H;
            case "I":
                return Keyboard.KEY_I;
            case "J":
                return Keyboard.KEY_J;
            case "K":
                return Keyboard.KEY_K;
            case "L":
                return Keyboard.KEY_L;
            case "M":
                return Keyboard.KEY_M;
            case "N":
                return Keyboard.KEY_N;
            case "O":
                return Keyboard.KEY_O;
            case "P":
                return Keyboard.KEY_P;
            case "Q":
                return Keyboard.KEY_Q;
            case "R":
                return Keyboard.KEY_R;
            case "S":
                return Keyboard.KEY_S;
            case "T":
                return Keyboard.KEY_T;
            case "U":
                return Keyboard.KEY_U;
            case "V":
                return Keyboard.KEY_V;
            case "W":
                return Keyboard.KEY_W;
            case "X":
                return Keyboard.KEY_X;
            case "Y":
                return Keyboard.KEY_Y;
            case "Z":
                return Keyboard.KEY_Z;
            case "0":
                return Keyboard.KEY_0;
            case "1":
                return Keyboard.KEY_1;
            case "2":
                return Keyboard.KEY_2;
            case "3":
                return Keyboard.KEY_3;
            case "4":
                return Keyboard.KEY_4;
            case "5":
                return Keyboard.KEY_5;
            case "6":
                return Keyboard.KEY_6;
            case "7":
                return Keyboard.KEY_7;
            case "8":
                return Keyboard.KEY_8;
            case "9":
                return Keyboard.KEY_9;
            case "F1":
                return Keyboard.KEY_F1;
            case "F2":
                return Keyboard.KEY_F2;
            case "F3":
                return Keyboard.KEY_F3;
            case "F4":
                return Keyboard.KEY_F4;
            case "F5":
                return Keyboard.KEY_F5;
            case "F6":
                return Keyboard.KEY_F6;
            case "F7":
                return Keyboard.KEY_F7;
            case "F8":
                return Keyboard.KEY_F8;
            case "F9":
                return Keyboard.KEY_F9;
            case "F10":
                return Keyboard.KEY_F10;
            case "F11":
                return Keyboard.KEY_F11;
            case "F12":
                return Keyboard.KEY_F12;
            case "ENTER":
                return Keyboard.KEY_RETURN;
            case "BACKSPACE":
                return Keyboard.KEY_BACK;
            case "TAB":
                return Keyboard.KEY_TAB;
            case "SHIFT":
                return Keyboard.KEY_LSHIFT;
            case "CTRL":
                return Keyboard.KEY_LCONTROL;
            case "ALT":
                return Keyboard.KEY_LMENU;
            case "CAPSLOCK":
                return Keyboard.KEY_CAPITAL;
            case "ESC":
                return Keyboard.KEY_ESCAPE;
            case "SPACE":
                return Keyboard.KEY_SPACE;
            case "PAGEUP":
                return Keyboard.KEY_PRIOR;
            case "PAGEDOWN":
                return Keyboard.KEY_NEXT;
            case "END":
                return Keyboard.KEY_END;
            case "HOME":
                return Keyboard.KEY_HOME;
            case "LEFT":
                return Keyboard.KEY_LEFT;
            case "UP":
                return Keyboard.KEY_UP;
            case "RIGHT":
                return Keyboard.KEY_RIGHT;
            case "DOWN":
                return Keyboard.KEY_DOWN;
            case "INSERT":
                return Keyboard.KEY_INSERT;
            case "DELETE":
                return Keyboard.KEY_DELETE;
            case "NUMLOCK":
                return Keyboard.KEY_NUMLOCK;
            case "SCROLLLOCK":
                return Keyboard.KEY_SCROLL;
            case "PRINTSCREEN":
                return Keyboard.KEY_SYSRQ;
            case "PAUSE":
                return Keyboard.KEY_PAUSE;
            default:
                return Keyboard.KEY_NONE;
        }
    }
}
