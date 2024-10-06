/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.injection.implementations.IMixinEntity;
import net.ccbluex.liquidbounce.utils.*;
import net.ccbluex.liquidbounce.utils.EntityUtils.isSelected;
import net.ccbluex.liquidbounce.utils.extensions.*;
import net.ccbluex.liquidbounce.utils.render.RenderUtils.drawBacktrackBox;
import net.ccbluex.liquidbounce.utils.render.RenderUtils.glColor;
import net.ccbluex.liquidbounce.utils.timing.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.server.*;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Backtrack extends Module {

    private final IntegerValue nextBacktrackDelay = new IntegerValue("NextBacktrackDelay", 0, 0, 2000) {
        @Override
        public IntegerValue onChange(IntegerValue oldValue, IntegerValue newValue) {
            if (mode.equals("Modern")) {
                clearPackets();
                reset();
            }
            return newValue;
        }
    };

    private final IntegerValue delay = new IntegerValue("Delay", 80, 0, 700) {
        @Override
        public IntegerValue onChange(IntegerValue oldValue, IntegerValue newValue) {
            if (mode.equals("Modern")) {
                clearPackets();
                reset();
            }
            return newValue;
        }
    };

    private final ListValue mode = new ListValue("Mode", new String[]{"Legacy", "Modern"}, "Modern") {
        @Override
        public ListValue onChanged(ListValue oldValue, ListValue newValue) {
            clearPackets();
            backtrackedPlayer.clear();
            return newValue;
        }
    };

    // Legacy
    private final ListValue legacyPos = new ListValue("Caching mode", new String[]{"ClientPos", "ServerPos"}, "ClientPos") {
        @Override
        public ListValue onChanged(ListValue oldValue, ListValue newValue) {
            if (mode.equals("Legacy")) {
                return newValue;
            }
            return oldValue;
        }
    };

    // Modern
    private final ListValue style = new ListValue("Style", new String[]{"Pulse", "Smooth"}, "Smooth") {
        @Override
        public ListValue onChanged(ListValue oldValue, ListValue newValue) {
            if (mode.equals("Modern")) {
                return newValue;
            }
            return oldValue;
        }
    };

    private final FloatValue maxDistanceValue = new FloatValue("MaxDistance", 3.0f, 0.0f, 3.5f) {
        @Override
        public FloatValue onChange(FloatValue oldValue, FloatValue newValue) {
            return newValue.coerceAtLeast(minDistance);
        }

        @Override
        public boolean isSupported() {
            return mode.equals("Modern");
        }
    };
    private final FloatValue maxDistance = maxDistanceValue;
    private final FloatValue minDistance = new FloatValue("MinDistance", 2.0f, 0.0f, 3.0f) {
        @Override
        public FloatValue onChange(FloatValue oldValue, FloatValue newValue) {
            return newValue.coerceIn(minimum, maxDistance);
        }

        @Override
        public boolean isSupported() {
            return mode.equals("Modern");
        }
    };
    private final BoolValue smart = new BoolValue("Smart", true) {
        @Override
        public boolean isSupported() {
            return mode.equals("Modern");
        }
    };

    // ESP
    private final ListValue espMode = new ListValue("ESP-Mode", new String[]{"None", "Box", "Player"}, "Box", true) {
        @Override
        public ListValue onChanged(ListValue oldValue, ListValue newValue) {
            if (mode.equals("Modern")) {
                return newValue;
            }
            return oldValue;
        }
    };
    private final BoolValue rainbow = new BoolValue("Rainbow", true, true) {
        @Override
        public boolean isSupported() {
            return mode.equals("Modern") && espMode.equals("Box");
        }
    };
    private final IntegerValue red = new IntegerValue("R", 0, 0, 255, true) {
        @Override
        public boolean isSupported() {
            return !rainbow.get() && mode.equals("Modern") && espMode.equals("Box");
        }
    };
    private final IntegerValue green = new IntegerValue("G", 255, 0, 255, true) {
        @Override
        public boolean isSupported() {
            return !rainbow.get() && mode.equals("Modern") && espMode.equals("Box");
        }
    };
    private final IntegerValue blue = new IntegerValue("B", 0, 0, 255, true) {
        @Override
        public boolean isSupported() {
            return !rainbow.get() && mode.equals("Modern") && espMode.equals("Box");
        }
    };

    private final LinkedHashMap<Packet<?>, Long> packetQueue = new LinkedHashMap<>();
    private final List<Pair<Vec3, Long>> positions = new ArrayList<>();

    private EntityLivingBase target;

    private final MSTimer globalTimer = new MSTimer();

    private boolean shouldRender = true;

    private boolean ignoreWholeTick = false;

    private long delayForNextBacktrack = 0L;

    // Legacy
    private final IntegerValue maximumCachedPositions = new IntegerValue("MaxCachedPositions", 10, 1, 20) {
        @Override
        public IntegerValue onChanged(IntegerValue oldValue, IntegerValue newValue) {
            if (mode.equals("Legacy")) {
                return newValue;
            }
            return oldValue;
        }
    };

    private final ConcurrentHashMap<UUID, List<BacktrackData>> backtrackedPlayer = new ConcurrentHashMap<>();

    private final String[] nonDelayedSoundSubstrings = new String[]{"game.player.hurt", "game.player.die"};

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();

        if (Blink.blinkingReceive()) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (mode.equals("Legacy")) {
            if (packet instanceof S0CPacketSpawnPlayer) {
                addBacktrackData(((S0CPacketSpawnPlayer) packet).getPlayer(), ((S0CPacketSpawnPlayer) packet).getPosX(), ((S0CPacketSpawnPlayer) packet).getPosY(), ((S0CPacketSpawnPlayer) packet).getPosZ(), System.currentTimeMillis());
            } else if (packet instanceof S14PacketEntity) {
                if (legacyPos.equals("ServerPos")) {
                    Entity entity = mc.theWorld.getEntityByID(((S14PacketEntity) packet).getEntityId());
                    IMixinEntity entityMixin = (IMixinEntity) entity;
                    if (entityMixin != null) {
                        addBacktrackData(entity.getUniqueID(), entityMixin.trueX, entityMixin.trueY, entityMixin.trueZ, System.currentTimeMillis());
                    }
                }
            } else if (packet instanceof S18PacketEntityTeleport) {
                if (legacyPos.equals("ServerPos")) {
                    Entity entity = mc.theWorld.getEntityByID(((S18PacketEntityTeleport) packet).getEntityId());
                    IMixinEntity entityMixin = (IMixinEntity) entity;
                    if (entityMixin != null) {
                        addBacktrackData(entity.getUniqueID(), entityMixin.trueX, entityMixin.trueY, entityMixin.trueZ, System.currentTimeMillis());
                    }
                }
            }
        } else if (mode.equals("Modern")) {
            if (packetQueue.isEmpty() && PacketUtils.queuedPackets.isEmpty() && !shouldBacktrack()) {
                return;
            }

            if (packet instanceof C00Handshake || packet instanceof C00PacketServerQuery || packet instanceof S02PacketChat || packet instanceof S01PacketPong) {
                return;
            }

            if (packet instanceof S08PacketPlayerPosLook || packet instanceof S40PacketDisconnect) {
                clearPackets();
                return;
            }

            if (packet instanceof S29PacketSoundEffect) {
                if (Arrays.asList(nonDelayedSoundSubstrings).contains(((S29PacketSoundEffect) packet).getSoundName())) {
                    return;
                }
            }

            if (packet instanceof S06PacketUpdateHealth) {
                if (((S06PacketUpdateHealth) packet).getHealth() <= 0) {
                    clearPackets();
                    return;
                }
            }

            if (packet instanceof S13PacketDestroyEntities) {
                if (target != null && Collections.singletonList(((S13PacketDestroyEntities) packet).getEntityIDs()).contains(target.getEntityId())) {
                    clearPackets();
                    reset();
                    return;
                }
            }

            if (packet instanceof S1CPacketEntityMetadata) {
                if (target.getEntityId() == ((S1CPacketEntityMetadata) packet).getEntityId()) {
                    List<NBTTagCompound> metadata = ((S1CPacketEntityMetadata) packet).func_149376_c();
                    if (metadata != null) {
                        for (NBTBase nbtBase : metadata) {
                            if (nbtBase.getDataType() == 6) {
                                double objectValue = nbtBase.getObject().toString().toDouble();
                                if (!Double.isNaN(objectValue) && objectValue <= 0.0) {
                                    clearPackets();
                                    reset();
                                    return;
                                }
                            }
                        }
                    }
                    return;
                }
            }

            if (packet instanceof S19PacketEntityStatus) {
                if (((S19PacketEntityStatus) packet).getEntityId() == target.getEntityId()) {
                    return;
                }
            }

            if (event.getEventType() == EventState.RECEIVE) {
                if (packet instanceof S14PacketEntity) {
                    if (((S14PacketEntity) packet).getEntityId() == target.getEntityId()) {
                        IMixinEntity targetMixin = (IMixinEntity) target;
                        synchronized (positions) {
                            positions.add(new Pair<>(new Vec3(targetMixin.trueX, targetMixin.trueY, targetMixin.trueZ), System.currentTimeMillis()));
                        }
                    }
                } else if (packet instanceof S18PacketEntityTeleport) {
                    if (((S18PacketEntityTeleport) packet).getEntityId() == target.getEntityId()) {
                        IMixinEntity targetMixin = (IMixinEntity) target;
                        synchronized (positions) {
                            positions.add(new Pair<>(new Vec3(targetMixin.trueX, targetMixin.trueY, targetMixin.trueZ), System.currentTimeMillis()));
                        }
                    }
                }
                event.cancelEvent();
                synchronized (packetQueue) {
                    packetQueue.put(packet, System.currentTimeMillis());
                }
            }
        }
    }

    @EventTarget
    public void onGameLoop(GameLoopEvent event) {
        if (mode.equals("Legacy")) {
            backtrackedPlayer.forEach((key, backtrackData) -> {
                backtrackData.removeIf(data -> data.time + delay.get() < System.currentTimeMillis());
                if (backtrackData.isEmpty()) {
                    removeBacktrackData(key);
                }
            });
        }

        EntityLivingBase target = this.target;
        IMixinEntity targetMixin = (IMixinEntity) target;
        if (mode.equals("Modern")) {
            if (targetMixin != null) {
                if (!Blink.blinkingReceive() && shouldBacktrack() && targetMixin.truePos) {
                    double trueDist = mc.thePlayer.getDistance(targetMixin.trueX, targetMixin.trueY, targetMixin.trueZ);
                    double dist = mc.thePlayer.getDistance(target.posX, target.posY, target.posZ);

                    if (trueDist <= 6f && (!smart.get() || trueDist >= dist) && (style.equals("Smooth") || !globalTimer.hasTimePassed(delay.get()))) {
                        shouldRender = true;

                        if (mc.thePlayer.getDistanceToEntityBox(target) >= minDistance.get() && mc.thePlayer.getDistanceToEntityBox(target) <= maxDistance.get()) {
                            handlePackets();
                        } else {
                            handlePacketsRange();
                        }
                    } else {
                        clearPackets();
                        globalTimer.reset();
                    }
                }
            } else {
                clearPackets();
                globalTimer.reset();
            }
        }

        ignoreWholeTick = false;
    }

    @EventTarget
    public void onAttack(AttackEvent event) {
        if (!isSelected(event.getTargetEntity(), true)) {
            return;
        }

        if (target != event.getTargetEntity()) {
            clearPackets();
            reset();
        }

        if (event.getTargetEntity() instanceof EntityLivingBase) {
            target = (EntityLivingBase) event.getTargetEntity();
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (mode.equals("Legacy")) {
            Color color = Color.RED;

            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer) {
                    glPushMatrix();
                    glDisable(GL_TEXTURE_2D);
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    glEnable(GL_LINE_SMOOTH);
                    glEnable(GL_BLEND);
                    glDisable(GL_DEPTH_TEST);

                    mc.entityRenderer.disableLightmap();

                    glBegin(GL_LINE_STRIP);
                    glColor(color);

                    double renderPosX = mc.renderManager.viewerPosX;
                    double renderPosY = mc.renderManager.viewerPosY;
                    double renderPosZ = mc.renderManager.viewerPosZ;

                    loopThroughBacktrackData(entity, () -> {
                        glVertex3d(entity.posX - renderPosX, entity.posY - renderPosY, entity.posZ - renderPosZ);
                        return false;
                    });

                    glColor4d(1.0, 1.0, 1.0, 1.0);
                    glEnd();
                    glEnable(GL_DEPTH_TEST);
                    glDisable(GL_LINE_SMOOTH);
                    glDisable(GL_BLEND);
                    glEnable(GL_TEXTURE_2D);
                    glPopMatrix();
                }
            }
        } else if (mode.equals("Modern")) {
            if (!shouldBacktrack() || packetQueue.isEmpty() || !shouldRender) {
                return;
            }

            if (!espMode.equals("Box")) {
                return;
            }

            RenderManager renderManager = mc.renderManager;

            target.run(() -> {
                IMixinEntity targetEntity = (IMixinEntity) target;
                if (targetEntity.truePos) {
                    double x = targetEntity.trueX - renderManager.renderPosX;
                    double y = targetEntity.trueY - renderManager.renderPosY;
                    double z = targetEntity.trueZ - renderManager.renderPosZ;

                    AxisAlignedBB axisAlignedBB = target.getEntityBoundingBox().offset(-target.posX, -target.posY, -target.posZ).offset(x, y, z);

                    drawBacktrackBox(AxisAlignedBB.fromBounds(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ), color);
                }
            });
        }
    }

    @EventTarget
    public void onEntityMove(EntityMovementEvent event) {
        if (mode.equals("Legacy") && legacyPos.equals("ClientPos")) {
            Entity entity = event.getMovedEntity();

            if (entity instanceof EntityPlayer) {
                addBacktrackData(entity.getUniqueID(), entity.posX, entity.posY, entity.posZ, System.currentTimeMillis());
            }
        }
    }

    @EventTarget
    public void onWorld(WorldEvent event) {
        if (mode.equals("Modern")) {
            if (event.getWorldClient() == null) {
                clearPackets(false);
            }
            target = null;
        }
    }

    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onDisable() {
        clearPackets();
        backtrackedPlayer.clear();
    }

    private void handlePackets() {
        synchronized (packetQueue) {
            packetQueue.entrySet().removeIf(entry -> entry.getValue() <= System.currentTimeMillis() - delay.get());
            PacketUtils.queuedPackets.addAll(packetQueue.keySet());
            packetQueue.clear();
        }
        synchronized (positions) {
            positions.removeIf(data -> data.second < System.currentTimeMillis() - delay.get());
        }
    }

    private void handlePacketsRange() {
        long time = getRangeTime();
        if (time == -1L) {
            clearPackets();
            return;
        }
        synchronized (packetQueue) {
            packetQueue.entrySet().removeIf(entry -> entry.getValue() <= time);
            PacketUtils.queuedPackets.addAll(packetQueue.keySet());
            packetQueue.clear();
        }
        synchronized (positions) {
            positions.removeIf(data -> data.second < time);
        }
    }

    private long getRangeTime() {
        if (target == null) {
            return 0L;
        }
        long time = 0L;
        boolean found = false;
        synchronized (positions) {
            for (Pair<Vec3, Long> data : positions) {
                time = data.second;
                Vec3 targetPos = new Vec3(target.posX, target.posY, target.posZ);
                double dx = data.first.xCoord - targetPos.xCoord;
                double dy = data.first.yCoord - targetPos.yCoord;
                double dz = data.first.zCoord - targetPos.zCoord;
                AxisAlignedBB targetBox = target.getEntityBoundingBox().offset(dx, dy, dz);
                if (mc.thePlayer.getDistanceToBox(targetBox) >= minDistance.get() && mc.thePlayer.getDistanceToBox(targetBox) <= maxDistance.get()) {
                    found = true;
                    break;
                }
            }
        }
        return found ? time : -1L;
    }

    private void clearPackets(boolean handlePackets) {
        if (!packetQueue.isEmpty()) {
            delayForNextBacktrack = System.currentTimeMillis() + nextBacktrackDelay.get();
        }

        synchronized (packetQueue) {
            if (handlePackets) {
                PacketUtils.queuedPackets.addAll(packetQueue.keySet());
            }
            packetQueue.clear();
        }

        positions.clear();
        shouldRender = false;
        ignoreWholeTick = true;
    }

    private void addBacktrackData(UUID id, double x, double y, double z, long time) {
        List<BacktrackData> backtrackData = getBacktrackData(id);

        if (backtrackData != null
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
