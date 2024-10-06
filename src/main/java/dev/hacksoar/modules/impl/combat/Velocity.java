package dev.hacksoar.modules.impl.combat;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventReceivePacket;
import dev.hacksoar.api.events.impl.EventUpdate;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.player.MoveUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;

@ModuleTag
public class Velocity extends Module {
    private final ListValue mode = new ListValue("Mode", new String[]{"Cancel", "Jump Reset", "Watchdog"}, "Cancel");
    private final BoolValue betterJump = new BoolValue("Better Jump Reset", true, () -> mode.isMode("Jump Reset"));

    private boolean jump = false;

    public Velocity() {
        super("Velocity", "No kb", ModuleCategory.Combat);
    }

    @Override
    public void onDisable() {
        jump = false;
        super.onDisable();
    }

    @EventTarget
    public void onRPacket(EventReceivePacket event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S12PacketEntityVelocity) {
            if (((S12PacketEntityVelocity) packet).entityID != mc.thePlayer.getEntityId()) {
                return;
            }
            switch (mode.get()) {
                case "Cancel":
                    event.setCancelled(true);
                    break;
                case "Watchdog":
                    event.setCancelled(true);
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = ((S12PacketEntityVelocity) packet).getMotionY() / 8000.0;
                        if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && MoveUtil.isMoving()) MoveUtil.strafe();
                    }
                    break;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.isMode("Jump Reset")) {
            if (!betterJump.get()) {
                if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.onGround) {
                    mc.gameSettings.keyBindJump.setPressed(true);
                    jump = true;
                } else if (jump) {
                    mc.gameSettings.keyBindJump.setPressed(false);
                    jump = false;
                }
                return;
            }

            if (mc.thePlayer.hurtTime >= 8) {
                mc.gameSettings.keyBindJump.setPressed(true);
            }
            if (mc.thePlayer.hurtTime >= 7) {
                mc.gameSettings.keyBindForward.setPressed(true);
            } else if (mc.thePlayer.hurtTime >= 4) {
                mc.gameSettings.keyBindJump.setPressed(false);
                mc.gameSettings.keyBindForward.setPressed(false);
            } else if (mc.thePlayer.hurtTime > 1) {
                mc.gameSettings.keyBindForward.setPressed(GameSettings.isKeyDown(mc.gameSettings.keyBindForward));
                mc.gameSettings.keyBindJump.setPressed(GameSettings.isKeyDown(mc.gameSettings.keyBindJump));
            }
        }
    }
}