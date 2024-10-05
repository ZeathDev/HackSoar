package dev.hacksoar.modules.impl.combat;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventPreMotion;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import net.minecraft.client.network.NetworkPlayerInfo;

@ModuleTag
public class AntiBot extends Module {
    public AntiBot() {
        super("AntiBot", "omg matrix bot", ModuleCategory.Combat);
    }

//    private final BoolValue advancedAntiBot = new BoolValue("Always Nearby Check", false);

    private final BoolValue watchdogAntiBot = new BoolValue("Watchdog Check", false);

/*    private final BoolValue funcraftAntiBot = new BoolValue("Funcraft Check", false);

    private final BoolValue ncps = new BoolValue("NPC Detection Check", false);

    private final BoolValue middleClick = new BoolValue("Middle Click Bot", false);*/

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (watchdogAntiBot.get()) {
            mc.theWorld.playerEntities.forEach(player -> {
                final NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(player.getUniqueID());

                if (info == null) {
                    HackSoar.instance.botManager.add(player);
                } else {
                    HackSoar.instance.botManager.remove(player);
                }
            });
        }
    }

    @Override
    public void onDisable() {
        HackSoar.instance.botManager.clear();
    }
}
