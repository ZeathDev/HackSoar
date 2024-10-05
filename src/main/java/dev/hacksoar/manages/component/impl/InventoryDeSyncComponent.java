package dev.hacksoar.manages.component.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventReceivePacket;
import dev.hacksoar.manages.component.Component;
import dev.hacksoar.utils.PlayerUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

public class InventoryDeSyncComponent extends Component {

    private static boolean active, deSynced;

    @EventTarget
    public void onPacketReceive(EventReceivePacket event) {
        Packet<?> p = event.getPacket();

        if (p instanceof S2DPacketOpenWindow) {
            if (active) {
                event.setCancelled(true);
                deSynced = true;
                active = false;
            }
        }
    };

    public static void setActive(String command) {
        if (active || deSynced || mc.currentScreen != null) {
            return;
        }

        PlayerUtils.tellPlayer(command);
        active = true;
    }

    public static boolean isDeSynced() {
        return deSynced;
    }
}
