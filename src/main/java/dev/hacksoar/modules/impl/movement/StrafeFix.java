package dev.hacksoar.modules.impl.movement;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventStrafe;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.manages.component.impl.RotationComponent;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import net.minecraft.util.MathHelper;

@ModuleTag
public class StrafeFix extends Module {
    public StrafeFix() {
        super("StrafeFix", "bypass grim move check", ModuleCategory.Movement);
    }

    @EventTarget
    public void onStrafe(EventStrafe event) {
        if (!RotationComponent.isActive()) return;

        runStrafeFixLoop(event);
    }

    public void runStrafeFixLoop(EventStrafe event) {
        if (event.isCancelled())
            return;

        float yaw = RotationComponent.rotations.x;
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        float factor = strafe * strafe + forward * forward;

        if (factor >= 1.0E-4F) {
            factor = MathHelper.sqrt_float(factor);
            if (factor < 1.0F) {
                factor = 1.0F;
            }

            factor = friction / factor;
            strafe *= factor;
            forward *= factor;

            float yawSin = MathHelper.sin((yaw * (float) Math.PI / 180F));
            float yawCos = MathHelper.cos((yaw * (float) Math.PI / 180F));

            mc.thePlayer.motionX += strafe * yawCos - forward * yawSin;
            mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin;
        }

        event.setCancelled(true);
    }
}
