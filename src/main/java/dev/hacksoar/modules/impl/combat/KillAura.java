package dev.hacksoar.modules.impl.combat;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.*;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.FloatValue;
import dev.hacksoar.api.value.impl.IntValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.manages.component.impl.BadPacketsComponent;
import dev.hacksoar.manages.component.impl.InventoryDeSyncComponent;
import dev.hacksoar.manages.component.impl.RotationComponent;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.MathUtils;
import dev.hacksoar.utils.TimerUtils;
import dev.hacksoar.utils.player.MovementFix;
import dev.hacksoar.utils.player.RayCastUtil;
import dev.hacksoar.utils.player.RotationUtil;
import dev.hacksoar.utils.player.StopWatch;
import dev.hacksoar.utils.render.RenderUtils;
import dev.hacksoar.utils.vector.Vector2f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;

import java.util.List;

@ModuleTag
public class KillAura extends Module {
    public KillAura() {
        super("KillAura", "Demo", ModuleCategory.Combat, Keyboard.KEY_R);
    }

    private final ListValue mode = new ListValue("Attack Mode", new String[]{"Single", "Switch", "Multiple"}, "Single");

    public static final ListValue autoBlock = new ListValue("Auto Block", new String[]{"None", "Fake", "Vanilla", "Vanilla Always", "NCP", "Watchdog", "Watchdog HvH", "Legit", "Intave", "Old Intave", "Imperfect Vanilla", "Vanilla ReBlock", "New NCP", "Block Hit"}, "None");
    private final IntValue blockhitDelay = new IntValue("BlockHit Delay", 120, 100, 300, () -> autoBlock.get().equals("Block Hit"));
    private final ListValue clickMode = new ListValue("Click Delay Mode", new String[]{"Normal", "Hit Select", "1.9+", "1.9+ (1.8 Visuals)"}, "Normal");

    private final FloatValue range = new FloatValue("Range", 3f, 3f, 8f);
    private final IntValue cpsA = new IntValue("CPS A", 10, 1, 20);
    private final IntValue cpsB = new IntValue("CPS B", 10, 1, 20);
    private final IntValue rotationSpeedA = new IntValue("Rotation speed A", 5, 0, 10);
    private final IntValue rotationSpeedB = new IntValue("Rotation speed B", 5, 0, 10);

    private final BoolValue keepSprint = new BoolValue("Keep sprint", false);

    private final BoolValue rayCast = new BoolValue("Ray cast", false);

    private final BoolValue advanced = new BoolValue("Advanced", false);
    private final BoolValue lookAtTheClosestPoint = new BoolValue("Look at the closest point on the player", true, advanced::get);
    private final BoolValue subTicks = new BoolValue("Attack outside ticks", false, advanced::get);
    private final ListValue rotationMode = new ListValue("Rotation Mode", new String[]{"Legit/Normal", "Autistic AntiCheat"}, "Legit/Normal", advanced::get);
    private final BoolValue attackWhilstScaffolding = new BoolValue("Attack whilst Scaffolding", false, advanced::get);
    private final BoolValue attackWhilstDisplayingGuis = new BoolValue("Attack whilst displaying Guis", false, advanced::get);
    private final BoolValue noSwing = new BoolValue("No swing", false, advanced::get);
    private final BoolValue autoDisable = new BoolValue("Auto disable", true, advanced::get);
    private final BoolValue grimFalse = new BoolValue("Prevent Grim false positives", false, advanced::get);
    private final BoolValue targetMark = new BoolValue("Target Mark", true, advanced::get);

    private final BoolValue showTargets = new BoolValue("Targets", true);
    public final BoolValue player = new BoolValue("Player", true, showTargets::get);
    public final BoolValue invisibles = new BoolValue("Invisibles", false, showTargets::get);
    public final BoolValue animals = new BoolValue("Animals", false, showTargets::get);
    public final BoolValue mobs = new BoolValue("Mobs", false, showTargets::get);
    public final BoolValue teams = new BoolValue("Teams", false, showTargets::get);


    private final StopWatch attackStopWatch = new StopWatch();
    private final StopWatch clickStopWatch = new StopWatch();

    private float randomYaw, randomPitch;
    private boolean swing, allowAttack;
    public static boolean blocking;
    private long nextSwing;

    public static List<Entity> targets;
    public static Entity target;

    public StopWatch subTicksStopWatch = new StopWatch();
    private int attack, hitTicks, expandRange;

    private final TimerUtils timeHelper = new TimerUtils();

    @EventTarget
    public void onPreMotion(EventPreMotion eventMotion) {
        this.hitTicks++;

        if (attackWhilstDisplayingGuis.get() && mc.currentScreen != null) {
            return;
        }

        if (target == null || mc.thePlayer.isDead || HackSoar.instance.moduleManager.getModule("Scaffold").getToggled()) {
            this.unblock(false);
            target = null;
        }
    }

    @Override
    public void onEnable() {
        this.attack = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        target = null;
        RotationComponent.stopRotation();
        this.unblock(false);
    }

    @EventTarget
    public void EventLoadWorld(EventLoadWorld eventLoadWorld) {
        if (this.autoDisable.get()) {
            this.toggle();
        }
    }

    public void getTargets(double range) {
        targets = HackSoar.instance.targetManager.getTargets(range);
    }

    @EventTarget
    public void onUpdate(EventPreUpdate eventUpdate) {
        if (mc.thePlayer.getHealth() <= 0.0 && this.autoDisable.get()) {
            this.toggle();
        }


        if (HackSoar.instance.moduleManager.getModule("Scaffold").getToggled() && !attackWhilstScaffolding.get()) {
            return;
        }

        this.attack = Math.max(Math.min(this.attack, this.attack - 2), 0);

        /*
         * Historic fix
         */
        if (mc.thePlayer.ticksExisted % 20 == 0) {
            expandRange = (int) (2 + Math.random() * 3);
        }

        if (mc.currentScreen != null) {
            return;
        }

        /*
         * Getting targets and selecting the nearest one
         */
        this.getTargets(range.get().doubleValue() + expandRange);

        if (targets.isEmpty()) {
//            this.randomiseTargetRotations();
            target = null;
            return;
        }

        target = targets.get(0);

        if (target == null || mc.thePlayer.isDead) {
//            this.randomiseTargetRotations();
            return;
        }

        if (this.canBlock()) {
            this.preBlock();
        }

        /*
         * Calculating rotations to target
         */
        this.rotations();

        /*
         * Doing the attack
         */
        this.doAttack(targets);

        /*
         * Blocking
         */
        if (this.canBlock()) {
            this.postAttackBlock();
        }
    }

    public void rotations() {
        final double minRotationSpeed = this.getMinRotationSpeed();
        final double maxRotationSpeed = this.getMaxRotationSpeed();
        final float rotationSpeed = (float) MathUtils.getRandom(minRotationSpeed, maxRotationSpeed);

        switch (rotationMode.get()) {
            case "Legit/Normal":
                final Vector2f targetRotations = RotationUtil.calculate(target, lookAtTheClosestPoint.get(), range.get().doubleValue());

                this.randomiseTargetRotations();

                targetRotations.x += randomYaw;
                targetRotations.y += randomPitch;

                if (RayCastUtil.rayCast(targetRotations, range.get().doubleValue()).typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
                    randomYaw = randomPitch = 0;
                }

                if (rotationSpeed != 0) {
                    RotationComponent.setRotations(targetRotations, rotationSpeed, MovementFix.OFF);
                }
                break;

            case "Autistic AntiCheat":
                double speed = rotationSpeed * 10;
                RotationComponent.setRotations(new Vector2f((float) (RotationComponent.rotations.x + speed), 0), speed / 18, MovementFix.OFF);
                break;
        }
    }

    /*
     * Randomising rotation target to simulate legit players
     */
    private void randomiseTargetRotations() {
        randomYaw += (float) (Math.random() - 0.5f);
        randomPitch += (float) (Math.random() - 0.5f) * 2;
    }

    @EventTarget
    public void onMouseOver(EventMouseOver event) {
        event.setRange(event.getRange() + range.get().doubleValue() - 3);
    }

    @EventTarget
    public void onPostMotion(EventPreMotion eventPreMotionUpdate) {
        if (target != null && this.canBlock()) {
            this.postBlock();
        }
    }

    private void doAttack(final List<Entity> targets) {
        String autoBlock = KillAura.autoBlock.get();
        if (BadPacketsComponent.bad(false, true, true, true, true) &&
                (autoBlock.equals("Fake") || autoBlock.equals("None") ||
                        autoBlock.equals("Imperfect Vanilla") || autoBlock.equals("Vanilla ReBlock") || autoBlock.equals("Block Hit"))) {
            return;
        }

        double delay = -1;
        boolean flag = false;

        switch (clickMode.get()) {
            case "Hit Select": {
                delay = 9;
                flag = target.hurtResistantTime <= 10;
                break;
            }

            case "1.9+": {
                double speed = 4;

                if (mc.thePlayer.getHeldItem() != null) {
                    final Item item = mc.thePlayer.getHeldItem().getItem();

                    if (item instanceof ItemSword) {
                        speed = 1.6;
                    } else if (item instanceof ItemSpade) {
                        speed = 1;
                    } else if (item instanceof ItemPickaxe) {
                        speed = 1.2;
                    } else if (item instanceof ItemAxe) {
                        switch (((ItemAxe) item).getToolMaterial()) {
                            case WOOD:
                            case STONE:
                                speed = 0.8;
                                break;

                            case IRON:
                                speed = 0.9;
                                break;

                            default:
                                speed = 1;
                                break;
                        }
                    } else if (item instanceof ItemHoe) {
                        switch (((ItemHoe) item).getToolMaterial()) {
                            case WOOD:
                            case GOLD:
                                speed = 1;
                                break;

                            case STONE:
                                speed = 2;
                                break;

                            case IRON:
                                speed = 3;
                                break;
                        }
                    }
                }

                delay = 1 / speed * 20 - 1;
                break;
            }
        }

        if (attackStopWatch.finished(this.nextSwing) && (!grimFalse.get() || !(mc.thePlayer.ticksSprint <= 1 && mc.thePlayer.isSprinting())) && !BadPacketsComponent.bad(false, true, true, false, true) && target != null && (clickStopWatch.finished((long) (delay * 50)) || flag)) {
            final long clicks = (long) (Math.round(MathUtils.getRandom(getMinCps(), getMaxCps())) * 1.5);
            this.nextSwing = 1000 / clicks;

            if (Math.sin(nextSwing) + 1 > Math.random() || attackStopWatch.finished(this.nextSwing) || Math.random() > 0.5) {
                this.allowAttack = true;

                if (this.canBlock()) {
                    this.attackBlock();
                }

                if (this.allowAttack) {
                    /*
                     * Attacking target
                     */
                    final double range = this.range.get().doubleValue();
                    final MovingObjectPosition movingObjectPosition = mc.objectMouseOver;

                    switch (this.mode.get()) {
                        case "Single": {
                            if ((mc.thePlayer.getDistanceToEntity(target) <= range && !rayCast.get()) ||
                                    (rayCast.get() && movingObjectPosition != null && movingObjectPosition.entityHit == target)) {
                                this.attack(target);
                            } else if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                                this.attack(movingObjectPosition.entityHit);
                            } else {
                                switch (clickMode.get()) {
                                    case "Normal":
                                    case "Hit Select":
                                        mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                                        this.clickStopWatch.reset();
                                        this.hitTicks = 0;
                                        break;
                                }
                            }
                            break;
                        }

                        case "Multiple": {
                            targets.removeIf(target -> mc.thePlayer.getDistanceToEntity(target) > range);

                            if (!targets.isEmpty()) {
                                targets.forEach(this::attack);
                            }
                            break;
                        }
                    }
                }

                this.attackStopWatch.reset();
            }
        }
    }

    private void attack(final Entity target) {
        this.attack = Math.min(Math.max(this.attack, this.attack + 2), 5);

        // Client.INSTANCE.getEventBus().handle(new ClickEvent());
        if (!this.noSwing.get())
            mc.thePlayer.swingItem();

        EventAttackEntity event = new EventAttackEntity(target);
        event.call();

        if (!event.isCancelled()) {
            if (this.keepSprint.get()) {
                mc.playerController.syncCurrentPlayItem();

                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(event.getEntity(), C02PacketUseEntity.Action.ATTACK));

                if (mc.thePlayer.fallDistance > 0 && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null) {
                    mc.thePlayer.onCriticalHit(target);
                }
            } else {
                mc.playerController.attackEntity(mc.thePlayer, target);
            }
        }

        if (autoBlock.get().equals("Block Hit") && canBlock()) {
            if (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && timeHelper.delay(blockhitDelay.get().longValue())) {
                mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                timeHelper.reset();
            }
        }

        this.clickStopWatch.reset();
        this.hitTicks = 0;
        //if (!pastTargets.contains(target)) pastTargets.add(target);
    }

    private void block(final boolean check, final boolean interact) {
        if (!blocking || !check) {
            if (interact && target != null && mc.objectMouseOver.entityHit == target) {
                mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
            }

            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.getItemStack()));
            blocking = true;
        }
    }

    private void unblock(final boolean swingCheck) {
        if (blocking && (!swingCheck || !swing)) {
            if (!mc.gameSettings.keyBindUseItem.isKeyDown()) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            } else {
                mc.gameSettings.keyBindUseItem.setPressed(false);
            }
            blocking = false;
        }
        RotationComponent.stopRotation();
    }

    @EventTarget
    public void onRenderItem(EventRenderItemPre event) {
        if (target != null && !(autoBlock.get().equals("None") || autoBlock.get().equals("Block Hit")) && this.canBlock()) {
            event.setEnumAction(EnumAction.BLOCK);
            event.setUseItem(true);
        }
    }

    @EventTarget
    public void onPacketSend(EventSendPacket event) {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C0APacketAnimation) {
            swing = true;
        } else if (packet instanceof C03PacketPlayer) {
            swing = false;
        }

//        if ((packet instanceof C0APacketAnimation || packet instanceof C02PacketUseEntity) && this.mode.get().equals("1.9+ (1.8 Visuals)")) {
//
//        }

        this.packetBlock(event);
    }

    public void packetBlock(final EventSendPacket event) {
        final Packet<?> packet = event.getPacket();

        switch (autoBlock.get()) {
            case "Intave":
                if (packet instanceof C03PacketPlayer) {
                    event.setCancelled(true);
                    this.unblock(false);
                    mc.getNetHandler().addToSendQueueUnregistered(packet);
                    this.block(false, true);
                }
                break;

            // All this no break
            case "Fake":
            case "None":
            case "Block Hit":
                if (this.getItemStack() == null || !(this.getItemStack().getItem() instanceof ItemSword)) {
                    return;
                }

                if (packet instanceof C08PacketPlayerBlockPlacement) {
                    final C08PacketPlayerBlockPlacement wrapper = (C08PacketPlayerBlockPlacement) packet;

                    if (wrapper.getPlacedBlockDirection() == 255) {
                        event.setCancelled(true);
                    }
                } else if (packet instanceof C07PacketPlayerDigging) {
                    C07PacketPlayerDigging wrapper = ((C07PacketPlayerDigging) packet);

                    if (wrapper.getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                        event.setCancelled(true);
                    }
                }
                break;

        }
    }

    private void attackBlock() {
        if ("Legit".equals(autoBlock.get())) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                mc.gameSettings.keyBindUseItem.setPressed(false);
            }


            this.allowAttack = !BadPacketsComponent.bad(false, false, false, true, false);
        }
    }

    private void postAttackBlock() {
        switch (autoBlock.get()) {
            case "Legit":
                if (this.hitTicks == 1) {
                    mc.gameSettings.keyBindUseItem.setPressed(true);
                    blocking = true;
                }
                break;

            case "Intave":
                this.block(false, false);
                break;

            case "Vanilla":
                if (this.hitTicks != 0) {
                    this.block(false, true);
                }
                break;

            case "Imperfect Vanilla":
                if (this.hitTicks == 1 && mc.thePlayer.isSwingInProgress && Math.random() > 0.1) {
                    this.block(false, true);
                }
                break;

            case "Vanilla ReBlock":
                if (this.hitTicks == 1 || !blocking) {
                    this.block(false, true);
                }
                break;

            case "Watchdog":
                if (mc.thePlayer.ticksSincePlayerVelocity >= 5 + (Math.random() * 4) && mc.thePlayer.ticksSincePlayerVelocity <= 20 && !blocking) {
                    this.block(true, true);
                }

                if (mc.thePlayer.ticksSincePlayerVelocity >= 16 + (Math.random() * 4) && blocking) {
                    this.unblock(true);
                }
                break;

            case "Watchdog HvH":
                mc.gameSettings.keyBindUseItem.setPressed(true);
                if ((this.hitTicks == 1 || !blocking) && !BadPacketsComponent.bad(false, true, true, true, false)) {
                    this.block(false, true);
                }
                break;
        }
    }


    @EventTarget
    public void onSlowDown(EventSlowDown event) {
        if (autoBlock.get().equals("Watchdog HvH")) {
            event.setCancelled(false);
            event.setStrafeMultiplier(0.2F);
            event.setForwardMultiplier(0.2F);
        }
    }

    private void preBlock() {
        switch (autoBlock.get()) {
            case "NCP":
            case "Intave":
                this.unblock(false);
                break;

            case "New NCP":
                if (blocking) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.getItemIndex() % 8 + 1));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.getItemIndex()));
                    blocking = false;
                }
                break;

            case "Old Intave":
//                InventoryDeSyncComponent.setActive("/booster");

                if (mc.thePlayer.isUsingItem()) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.getItemIndex() % 8 + 1));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.getItemIndex()));
                }
                break;
        }
    }

    private void postBlock() {
        switch (autoBlock.get()) {
            case "Vanilla Always":
                this.block(false, true);
                break;
            case "NCP":
            case "New NCP":
                this.block(true, false);
                break;

            case "Old Intave":
                if (mc.thePlayer.isUsingItem() && InventoryDeSyncComponent.isDeSynced()) {
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.getItemStack()));
                }
                break;
        }
    }

    private boolean canBlock() {
        return this.getItemStack() != null && this.getItemStack().getItem() instanceof ItemSword;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (this.subTicks.get() && this.attack <= 5 && target != null && this.subTicksStopWatch.finished(10)) {
            this.subTicksStopWatch.reset();

            /*
             * Getting targets and selecting the nearest one
             */
            targets = HackSoar.instance.targetManager.getTargets(range.get().doubleValue() + expandRange);

            if (targets.isEmpty()) {
//                this.randomiseTargetRotations();
                target = null;
                // RotationComponent.stopRotation();
                return;
            }

            this.doAttack(targets);
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (targetMark.get() && target != null) {
            RenderUtils.drawTargetCapsule(target, 0.67, true);
        }
    }

    private int getMaxCps() {
        return Math.max(cpsA.get(), cpsB.get());
    }

    private int getMinCps() {
        return Math.min(cpsA.get(), cpsB.get());
    }

    public int getMaxRotationSpeed() {
        return Math.max(rotationSpeedA.get(), rotationSpeedB.get());
    }

    public int getMinRotationSpeed() {
        return Math.min(rotationSpeedA.get(), rotationSpeedB.get());
    }

    public ItemStack getItemStack() {
        return (mc.thePlayer == null || mc.thePlayer.inventoryContainer == null ? null : mc.thePlayer.inventoryContainer.getSlot(getItemIndex() + 36).getStack());
    }

    public int getItemIndex() {
        final InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
        return /*inventoryPlayer.alternativeSlot || !animation.isFinished() ? inventoryPlayer.alternativeCurrentItem : */inventoryPlayer.currentItem;
    }
}
