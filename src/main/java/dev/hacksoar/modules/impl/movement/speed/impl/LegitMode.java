package dev.hacksoar.modules.impl.movement.speed.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventPreUpdate;
import dev.hacksoar.api.events.impl.EventStrafe;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.manages.component.impl.RotationComponent;
import dev.hacksoar.modules.impl.movement.speed.SpeedMode;
import dev.hacksoar.utils.player.MoveUtil;
import dev.hacksoar.utils.player.MovementFix;
import dev.hacksoar.utils.vector.Vector2f;

@ModuleTag
public class LegitMode extends SpeedMode {
    public LegitMode() {
        super("Legit");
    }

    private final ListValue rotationExploit = new ListValue("Rotation Exploit Mode", new String[]{"Off","Rotate","Speed Equivalent"},"Speed Equivalent");
    private final BoolValue cpuSpeedUpExploit = new BoolValue("CPU SpeedUp Exploit", true);
    private final BoolValue noJumpDelay = new BoolValue("No Jump Delay", true);

    @EventTarget
    public void onPreUpdate(EventPreUpdate event) {
        switch (rotationExploit.get()) {
            case "Rotate":
                if (!mc.thePlayer.onGround)
                    RotationComponent.setRotations(new Vector2f(mc.thePlayer.rotationYaw + 45, mc.thePlayer.rotationPitch), 10, MovementFix.NORMAL);
                break;

            case "Speed Equivalent (Almost legit, Very hard to flag)":
                MoveUtil.useDiagonalSpeed();
                break;
        }

        if (noJumpDelay.getValue()) {
            mc.thePlayer.jumpTicks = 0;
        }

        if (cpuSpeedUpExploit.getValue()) {
            mc.timer.timerSpeed = 1.004f;
        }
    }

    @EventTarget
    public void strafe(EventStrafe event) {
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
        }
    }
}
