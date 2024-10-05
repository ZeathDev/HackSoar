package dev.hacksoar.modules.impl.utilty;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventUpdate;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.IntValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.player.PacketUtil;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleTag
public class FastUse extends Module {
    private final ListValue mode = new ListValue("Mode", new String[]{"Vanilla", "Packet"}, "Vanilla");
    private final IntValue packets = new IntValue("Packets", 20, 1, 35, () -> mode.isMode("Packet"));

    public FastUse() {
        super("FastUse", "eat fast", ModuleCategory.Util);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemAppleGold || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion)) {
            switch (mode.get().toLowerCase()) {
                case "vanilla":
                    for (int i = 0; i < 32; ++i) {
                        PacketUtil.send(new C03PacketPlayer(mc.thePlayer.onGround));
                    }
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    break;
                case "packet":
                    for (int i = 0; i < packets.getValue(); ++i) {
                        PacketUtil.send(new C03PacketPlayer(mc.thePlayer.onGround));
                    }
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    break;
            }
        }
    }
}
