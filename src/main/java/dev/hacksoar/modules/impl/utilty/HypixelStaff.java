package dev.hacksoar.modules.impl.utilty;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventLoadWorld;
import dev.hacksoar.api.events.impl.EventReceivePacket;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.PlayerUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S3EPacketTeams;

import java.util.ArrayList;
import java.util.List;

/**
 * Code from rise 6.0
 */
@ModuleTag
public class HypixelStaff extends Module {
    public HypixelStaff() {
        super("HypixelStaff","Check hypixel staffs", ModuleCategory.Util);
    }

    private final List<String> fatPeople = new ArrayList<>();
    private final List<String> locPeople = new ArrayList<>();
    private boolean started;

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        final Packet<?> p = event.getPacket();

        if (p instanceof S3EPacketTeams) {
            final S3EPacketTeams packet = (S3EPacketTeams) p;
            if (mc.thePlayer.ticksExisted < 20) {
                started = false;
                locPeople.addAll(packet.getPlayers());
                return;
            }

            fatPeople.add("MCVisuals");
            fatPeople.add("Centranos");
            fatPeople.add("LeBrillant");
            fatPeople.add("Aerh");
            fatPeople.add("Gerbor12");
            fatPeople.add("mrkeith");
            fatPeople.add("Rhune");
            fatPeople.add("SnowyPai");
            fatPeople.add("Smoarzified");
            fatPeople.add("DeluxeRose");
            fatPeople.add("Greeenn");
            fatPeople.add("jamzs");
            fatPeople.add("JordWG");
            fatPeople.add("Phaige");
            fatPeople.add("Quack");
            fatPeople.add("Pensul");
            fatPeople.add("LadyBleu");
            fatPeople.add("Fr0z3n");
            fatPeople.add("Citria");
            fatPeople.add("TheBirmanator");
            fatPeople.add("TorWolf");
            fatPeople.add("Minikloon");
            fatPeople.add("Rozsa");
            fatPeople.add("The_Darthonian");

            for (final String name : packet.getPlayers()) {
                if (started && !locPeople.contains(name)) {
                    PlayerUtils.tellPlayer(name + " joined late (possible staff member?)");
                }

                if (fatPeople.contains(name)) {
                    for (int i = 0; i < 10; i++) {
                        PlayerUtils.tellPlayer("Staff detected in data " + name + " TEAM NAME: " + packet.getDisplayName());
                    }
                    mc.thePlayer.playSound("random.click",0.5F,1F);
                }
            }
        }

        if (p instanceof S02PacketChat) {
            final S02PacketChat wrapper = (S02PacketChat) p;
            final String message = wrapper.getChatComponent().getUnformattedText();

            if (!wrapper.isChat() && message.equals("Cages opened! FIGHT!")) {
                PlayerUtils.tellPlayer("Detected game start.");
                started = true;
            }
        }
    }


    @EventTarget
    public void onLoadWorld(EventLoadWorld eventLoadWorld) {
        started = false;
        locPeople.clear();
    }

    @Override
    public void onEnable() {
        started = false;
        locPeople.clear();
    }
}
