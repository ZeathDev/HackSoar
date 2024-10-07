package dev.hacksoar.modules.impl.movement.speed.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventPreMotion;
import dev.hacksoar.api.events.impl.EventStrafe;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.impl.movement.speed.SpeedMode;
import dev.hacksoar.utils.player.MoveUtil;
import dev.hacksoar.utils.player.PacketUtil;
import dev.hacksoar.utils.player.PlayerUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleTag
public class WatchdogMode extends SpeedMode {
    public WatchdogMode() {
        super("Watchdog");
    }

    public ListValue mode = new ListValue("Type",new String[]{"Full Strafe","Damage Strafe","Ground Strafe"},"Damage Strafe");

    private float angle;

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if ("Ground Strafe".equals(mode.get()) || "Damage Strafe".equals(mode.get())) {
            return;
        }

        if (!(PlayerUtils.blockRelativeToPlayer(0, -1, 0) instanceof BlockAir) && mc.thePlayer.ticksSinceVelocity > 20) {
            event.setOnGround(true);
        }
    }

    @EventTarget
    public void onStrafe(EventStrafe event) {
        if (mc.thePlayer.ticksSinceTeleport > 40) MoveUtil.useDiagonalSpeed();

        switch (mode.get()) {

            case "Ground Strafe":
                if (mc.thePlayer.onGround && mc.thePlayer.ticksSinceJump > 5 && MoveUtil.isMoving()) {
                    double lastAngle = Math.atan(mc.thePlayer.lastMotionX / mc.thePlayer.lastMotionZ) * (180 / Math.PI);

                    MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - Math.random() / 100f);
                    mc.thePlayer.jump();

                    double angle = Math.atan(mc.thePlayer.motionX / mc.thePlayer.motionZ) * (180 / Math.PI);

                    if (Math.abs(lastAngle - angle) > 20 && mc.thePlayer.ticksSinceVelocity > 20) {
                        int speed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;

                        switch (speed) {
                            case 0:
                                MoveUtil.moveFlying(-0.005);
                                break;

                            case 1:
                                MoveUtil.moveFlying(-0.035);
                                break;

                            default:
                                MoveUtil.moveFlying(-0.04);
                                break;
                        }
                    }
                }

                break;

            case "Damage Strafe":
                if (mc.thePlayer.onGround && mc.thePlayer.ticksSinceJump > 5 && MoveUtil.isMoving()) {
                    MoveUtil.strafe();
                    MoveUtil.strafe((MoveUtil.getAllowedHorizontalDistance() - Math.random() / 100f));
                    mc.thePlayer.jump();
                }

                if (mc.thePlayer.ticksSincePlayerVelocity <= 20 && mc.thePlayer.ticksSinceTeleport > 20) {
                    MoveUtil.strafe();
                }
                break;

            case "Full Strafe":
                if (!(PlayerUtils.blockRelativeToPlayer(0, -1, 0) instanceof BlockAir) || !(PlayerUtils.blockRelativeToPlayer(0, -1.1, 0) instanceof BlockAir)) {
                    angle = MoveUtil.simulationStrafeAngle(angle, mc.thePlayer.ticksSinceVelocity < 40 ? 39.9f : 19.9f);
                }

                if (mc.thePlayer.ticksSinceVelocity <= 20 || mc.thePlayer.onGround) {
                    angle = MoveUtil.simulationStrafeAngle(angle, 360);
                }

                PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));

                if (mc.thePlayer.ticksSinceVelocity > 20) {
                    switch (mc.thePlayer.offGroundTicks) {
                        case 1:
                            mc.thePlayer.motionY -= 0.005;
                            break;

                        case 2:
                        case 3:
                            mc.thePlayer.motionY -= 0.001;
                            break;
                    }
                }

                if (mc.thePlayer.onGround) {
                    double lastAngle = Math.atan(mc.thePlayer.lastMotionX / mc.thePlayer.lastMotionZ) * (180 / Math.PI);

                    MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - Math.random() / 1000);
                    mc.thePlayer.jump();

                    double angle = Math.atan(mc.thePlayer.motionX / mc.thePlayer.motionZ) * (180 / Math.PI);

                    if (Math.abs(lastAngle - angle) > 20 && mc.thePlayer.ticksSinceVelocity > 20) {
                        int speed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;

                        switch (speed) {
                            case 0:
                                MoveUtil.moveFlying(-0.005);
                                break;

                            case 1:
                                MoveUtil.moveFlying(-0.035);
                                break;

                            default:
                                MoveUtil.moveFlying(-0.04);
                                break;
                        }
                    }
                }
                break;
        }

        // mc.timer.timerSpeed = timer.getValue().floatValue();
    }
}
