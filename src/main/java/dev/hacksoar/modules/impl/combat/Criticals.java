package dev.hacksoar.modules.impl.combat;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventAttackEntity;
import dev.hacksoar.api.events.impl.EventSendPacket;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.MathUtils;
import dev.hacksoar.utils.player.PacketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleTag
public class Criticals extends Module {
    private final ListValue mode = new ListValue("Mode", new String[]{"Packet", "Edit", "DCJ Network", "Hop"}, "Packet");

    private boolean attacked = false;

    public Criticals() {
        super("Criticals", "dmg x1.5", ModuleCategory.Combat);
    }

    @Override
    public void onDisable() {
        attacked = false;
        super.onDisable();
    }

    @EventTarget
    public void onAttack(EventAttackEntity event) {
        if (!canCritical(event)) return;

        double x = mc.thePlayer.posX, y = mc.thePlayer.posY, z = mc.thePlayer.posZ;
        int hurttime = ((EntityLivingBase) event.getEntity()).hurtTime;

        switch (mode.get().toLowerCase()) {
            case "packet":
                PacketUtil.send(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01100007869, z, true));
                PacketUtil.send(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                break;
            case "edit":
                attacked = true;
                break;
            case "dcj network":
                if (mc.thePlayer.getDistanceToEntity(event.getEntity()) > 6.0) {
                    mc.thePlayer.setPosition(x, y + 0.3, z);
                } else {
                    if (hurttime < 4) {
                        mc.thePlayer.motionY = 0.21;
                        mc.thePlayer.fallDistance = 0.21f;
                    }
                }
                break;
            case "hop":
                mc.thePlayer.motionY = 0.07840000736198732;
                mc.thePlayer.fallDistance = 0.07840000736198732f;
                break;
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (mode.isMode("Edit")) {
            if (event.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
                if (attacked) {
                    packet.onGround = false;
                    attacked = false;
                }
            }
        }
    }

    private boolean canCritical(EventAttackEntity event) {
        final Entity entity = event.getEntity();

        if (!(entity instanceof EntityLivingBase) || mc.thePlayer == null) return false;

        return mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && !mc.thePlayer.isInWeb;
    }
}
