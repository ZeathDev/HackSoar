package dev.hacksoar.modules.impl.utilty;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.*;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.FloatValue;
import dev.hacksoar.api.value.impl.IntValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.manages.component.impl.BadPacketsComponent;
import dev.hacksoar.manages.component.impl.BlinkComponent;
import dev.hacksoar.manages.component.impl.RotationComponent;
import dev.hacksoar.manages.component.impl.SlotComponent;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.MathUtils;
import dev.hacksoar.utils.PlayerUtils;
import dev.hacksoar.utils.invs.SlotUtil;
import dev.hacksoar.utils.player.*;
import dev.hacksoar.utils.vector.Vector2f;
import dev.hacksoar.utils.vector.Vector3d;
import net.minecraft.block.BlockAir;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.Objects;

/**
 * @author: Liycxc
 */
public class Scaffold extends Module {

    public final BoolValue movementCorrection = new BoolValue("Movement Correction", false);
    public final BoolValue safeWalk = new BoolValue("Safe Walk", true);
    private final ListValue mode = new ListValue("Mode", new String[]{"Normal","Snap","Telly","UPDATED-NCP"},"Normal");
    private final ListValue rayCast = new ListValue("Ray Cast", new String[]{"Off","Normal","Strict"},"Off");
    private final ListValue sprint = new ListValue("Sprint", new String[]{"Normal","Disabled","Legit","Bypass","Vulcan", "Matrix","Watchdog"},"Normal");
    private final FloatValue vulcanSpeed = new FloatValue("Vulcan Speed",1.3f,0.9f,1.3f,() -> "Vulcan".equals(sprint.get()));
    private final ListValue tower = new ListValue("Tower", new String[]{"Disabled","Vulcan","Vanilla","Normal","Air Jump","Watchdog","MMC","NCP","Matrix","Legit"},"Disabled");
    private final ListValue sameY = new ListValue("Same Y", new String[]{"Off","On","Auto Jump"},"Off");
    private final IntValue rotationSpeedA = new IntValue("Rotation Speed A", 5, 0, 10);
    private final IntValue rotationSpeedB = new IntValue("Rotation Speed B", 10, 0, 10);
    private final IntValue placeDelayA = new IntValue("Place Delay A", 0, 0, 5);
    private final IntValue placeDelayB = new IntValue("Place Delay B", 0,0,5);
    private final FloatValue timer = new FloatValue("Timer", 1f, 0.1f, 10f);
    private final BoolValue sneak = new BoolValue("Sneak", false);
    public final IntValue startSneakingA = new IntValue("Start Sneaking A", 0, 0, 5, sneak::get);
    public final IntValue startSneakingB = new IntValue("Start Sneaking B", 0, 0, 5, sneak::get);
    public final IntValue stopSneakingA = new IntValue("Stop Sneaking A", 0, 0, 5, sneak::get);
    public final IntValue stopSneakingB = new IntValue("Stop Sneaking B", 0, 0, 5, sneak::get);
    public final IntValue sneakEvery = new IntValue("Sneak every x blocks", 1, 1, 10, sneak::get);
    public final FloatValue sneakingSpeed = new FloatValue("Sneaking Speed", 0.2f, 0.2f, 1f, sneak::get);
    private final BoolValue render = new BoolValue("Render", true);
    private final BoolValue advanced = new BoolValue("Advanced", false);
    public final ListValue yawOffset = new ListValue("Yaw Offset", new String[]{"0","45","-45"},"0", advanced::get);
    public final BoolValue ignoreSpeed = new BoolValue("Ignore Speed Effect", false, advanced::get);
    public final BoolValue upSideDown = new BoolValue("Up Side Down", false, advanced::get);
    private Vec3 targetBlock;
    private EnumFacingOffset enumFacing;
    private BlockPos blockFace;
    private float targetYaw, targetPitch;
    private int ticksOnAir, sneakingTicks;
    private double startY;
    private float forward, strafe;
    private int placements;
    private boolean incrementedPlacements;
    // sprints
    private int vulcanTime, vulcanBlock;
    private int MatrixTime;
    private int WatchdogBlocks;
    private boolean sprintState = false;
    public Scaffold() {
        super("Scaffold","Make you fly in your way", ModuleCategory.Util);
    }

    @Override
    public void onEnable() {
        targetYaw = mc.thePlayer.rotationYaw - 180;
        targetPitch = 90;

        startY = Math.floor(mc.thePlayer.posY);
        targetBlock = null;

        this.sneakingTicks = -1;
        if ("Watchdog".equals(sprint.get())) {
            if (WatchdogBlocks <= 0) {
                WatchdogBlocks = 0;
                mc.gameSettings.keyBindSprint.setPressed(true);
                mc.thePlayer.setSprinting(true);
            }
        }

        sprintState = HackSoar.instance.modManager.getModByName("Sprint").isToggled();

        if ("Disabled".equals(sprint.get()) && sprintState) {
            HackSoar.instance.modManager.getModByName("Sprint").setToggled(false);
        }
    }

    @Override
    public void onDisable() {
        // sprint
        if ("Vulcan".equals(sprint.get())) {
            if (vulcanTime == 1) {
                PacketUtil.send(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
        mc.gameSettings.keyBindSneak.setPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
        mc.gameSettings.keyBindJump.setPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));

        BlinkComponent.blinking = false;

        // This is a temporary patch
        SlotComponent.setSlot(mc.thePlayer.inventory.currentItem);

        if (mc.timer.timerSpeed != 1f){
            mc.timer.timerSpeed = 1f;
        }

        if (sprintState) {
            HackSoar.instance.modManager.getModByName("Sprint").setToggled(true);
        }

        if (safeWalk.get() && mc.thePlayer.safeWalk) {
            mc.thePlayer.safeWalk = false;
        }
    }
    
    @EventTarget
    public void onPacketReceiveEvent(EventReceivePacket event) {

        final Packet<?> packet = event.getPacket();

        if (packet instanceof S2FPacketSetSlot) {
            final S2FPacketSetSlot wrapper = ((S2FPacketSetSlot) packet);

            if (wrapper.func_149174_e() == null) {
                event.setCancelled(true);
            } else {
                try {
                    int slot = wrapper.func_149173_d() - 36;
                    if (slot < 0) {
                        return;
                    }
                    final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
                    final Item item = wrapper.func_149174_e().getItem();

                    if ((itemStack == null && wrapper.func_149174_e().stackSize <= 6 && item instanceof ItemBlock && !SlotUtil.blacklist.contains(((ItemBlock) item).getBlock())) ||
                            itemStack != null && Math.abs(Objects.requireNonNull(itemStack).stackSize - wrapper.func_149174_e().stackSize) <= 6 ||
                            wrapper.func_149174_e() == null) {
                        event.setCancelled(true);
                    }
                } catch (ArrayIndexOutOfBoundsException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @EventTarget
    public void onSpendPacket(EventSendPacket event) {
        final Packet<?> p = event.getPacket();
        switch (sprint.get()) {
            case "Vulcan": {
                if (p instanceof C08PacketPlayerBlockPlacement) {
                    final C08PacketPlayerBlockPlacement wrapper = (C08PacketPlayerBlockPlacement) p;

                    if (wrapper.getPlacedBlockDirection() != 255) {
                        vulcanBlock = 0;
                    }
                }
                break;
            }
            case "Matrix": {
                if (p instanceof C08PacketPlayerBlockPlacement) {
                    final C08PacketPlayerBlockPlacement wrapper = (C08PacketPlayerBlockPlacement) p;

                    if (wrapper.getPlacedBlockDirection() != 255) {
                        MatrixTime = 0;
                    }
                }
                break;
            }
            case "Watchdog": {
                if (p instanceof C08PacketPlayerBlockPlacement) {
                    C08PacketPlayerBlockPlacement wrapper = (C08PacketPlayerBlockPlacement) p;
                    if (wrapper.getPlacedBlockDirection() != 255) {
                        if (mc.thePlayer.isSprinting()) {
                            WatchdogBlocks++;
                        } else {
                            WatchdogBlocks -= 2;
                        }
                    }
                }
                break;
            }
            default: {
                break;
            }
        }

        final Packet<?> packet = event.getPacket();
        // tower
        switch (tower.get()) {
            case "Vulcan": {
                if (mc.thePlayer.motionY > -0.0784000015258789 && packet instanceof C08PacketPlayerBlockPlacement) {
                    final C08PacketPlayerBlockPlacement wrapper = ((C08PacketPlayerBlockPlacement) packet);

                    if (wrapper.getPosition().equals(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.4, mc.thePlayer.posZ))) {
                        mc.thePlayer.motionY = -0.0784000015258789;
                    }
                }
                break;
            }
            case "Watchdog": {
                if (mc.thePlayer.motionY > -0.0784000015258789 && !mc.thePlayer.isPotionActive(Potion.jump) && packet instanceof C08PacketPlayerBlockPlacement && MoveUtil.isMoving()) {
                    final C08PacketPlayerBlockPlacement wrapper = ((C08PacketPlayerBlockPlacement) packet);

                    if (wrapper.getPosition().equals(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.4, mc.thePlayer.posZ))) {
                        mc.thePlayer.motionY = -0.0784000015258789;
                    }
                }
                break;
            }
            case "MMC": {
                if (mc.gameSettings.keyBindJump.isKeyDown() && packet instanceof C08PacketPlayerBlockPlacement) {
                    final C08PacketPlayerBlockPlacement c08PacketPlayerBlockPlacement = ((C08PacketPlayerBlockPlacement) packet);

                    if (c08PacketPlayerBlockPlacement.getPosition().equals(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.4, mc.thePlayer.posZ))) {
                        mc.gameSettings.keyBindSprint.setPressed(false);
                        mc.thePlayer.setSprinting(false);
                        mc.thePlayer.motionY = 0.42F;
                    }
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    @EventTarget
    public void onPreMotionEvent(EventPreMotion event) {
        // sprint
        switch (sprint.get()) {
            case "Normal": {
                break;
            }
            case "Disabled": {

                mc.gameSettings.keyBindSprint.setPressed(false);
                mc.thePlayer.setSprinting(false);
                break;
            }
            case "Legit": {
                if (Math.abs(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) - MathHelper.wrapAngleTo180_float(RotationComponent.rotations.x)) > 90) {
                    mc.gameSettings.keyBindSprint.setPressed(false);
                    mc.thePlayer.setSprinting(false);
                }
                break;
            }
            case "Bypass": {
                if (MoveUtil.isMoving() && mc.thePlayer.isSprinting() && mc.thePlayer.onGround) {
                    final double speed = MoveUtil.WALK_SPEED;
                    final float yaw = (float) MoveUtil.direction();
                    final double posX = MathHelper.sin(yaw) * speed + mc.thePlayer.posX;
                    final double posZ = -MathHelper.cos(yaw) * speed + mc.thePlayer.posZ;
                    PacketUtil.send(new C03PacketPlayer.C04PacketPlayerPosition(posX, event.getPosY(), posZ, false));
                }
                //   mc.thePlayer.setSprinting(false);
                break;
            }
            case "Vulcan": {
                mc.thePlayer.setSprinting(false);

                final double speed = vulcanSpeed.get().doubleValue();

                if (!mc.gameSettings.keyBindJump.isKeyDown() && speed > 0.9) {
                    if (mc.thePlayer.onGroundTicks >= 2 && vulcanBlock <= 10) {
                        MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * speed);

                        mc.thePlayer.jump();
                        mc.thePlayer.motionY = 0.012500047683714;
                    }
                }

                if (mc.thePlayer.onGround) {
                    MoveUtil.strafe();
                }

                vulcanTime++;
                vulcanBlock++;

                switch (vulcanTime) {
                    case 1:
                        PacketUtil.send(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        break;

                    case 10:
                        vulcanTime = 0;
                        break;
                }
                break;
            }
            case "Matrix": {
                MatrixTime++;

                mc.gameSettings.keyBindSneak.setPressed(MatrixTime >= 4);
                break;
            }
            case "Watchdog": {
                if (mc.thePlayer.onGround) {
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        mc.thePlayer.motionX *= 1.01 - Math.random() / 100f;
                        mc.thePlayer.motionZ *= 1.01 - Math.random() / 100f;
                    } else {
                        mc.thePlayer.setSprinting(false);
                        mc.gameSettings.keyBindSprint.setPressed(false);
                    }
                } else {
                    mc.thePlayer.setSprinting(false);
                    mc.gameSettings.keyBindSprint.setPressed(false);
                }

                if (!mc.thePlayer.isPotionActive(Potion.moveSpeed) && MoveUtil.speed() > 0.113 && mc.thePlayer.onGround) {
                    MoveUtil.strafe(0.113 - Math.random() / 100f);
                }
                break;
            }
        }

        // tower
        switch (tower.get()) {
            case "Vulcan": {
                if (mc.gameSettings.keyBindJump.isKeyDown() && PlayerUtils.blockNear(2) && mc.thePlayer.offGroundTicks > 3) {
                    ItemStack itemStack = mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem];

                    if (itemStack == null || (itemStack.stackSize > 2)) {
                        PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(null));
                    }
                    mc.thePlayer.motionY = 0.42F;
                }
                break;
            }
            case "Vanilla": {
                if (mc.gameSettings.keyBindJump.isKeyDown() && PlayerUtils.blockNear(2)) {
                    mc.thePlayer.motionY = 0.42F;
                }
                break;
            }
            case "Normal": {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.42F;
                    }
                }
                break;
            }
            case "Air Jump": {
                if (mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.ticksExisted % 2 == 0 && PlayerUtils.blockNear(2)) {
                    mc.thePlayer.motionY = 0.42F;
                    event.setOnGround(true);
                }
                break;
            }
            case "NCP": {
                if (mc.gameSettings.keyBindJump.isKeyDown() && PlayerUtils.blockNear(2)) {
                    PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(null));

                    // mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;

                    if (mc.thePlayer.posY % 1 <= 0.00153598) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
                        mc.thePlayer.motionY = 0.42F;
                    } else if (mc.thePlayer.posY % 1 < 0.1 && mc.thePlayer.offGroundTicks != 0) {
                        mc.thePlayer.motionY = 0;
                        mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
                    }
                }
                break;
            }
            case "Matrix": {
                if (mc.gameSettings.keyBindJump.isKeyDown() && PlayerUtils.isBlockUnder(2, false) && mc.thePlayer.motionY < 0.2) {
                    mc.thePlayer.motionY = 0.42F;
                    event.setOnGround(true);
                }
                break;
            }

            default: {
                break;
            }
        }


        if (targetBlock == null || enumFacing == null || blockFace == null) {
            return;
        }

        mc.thePlayer.hideSneakHeight.reset();

        // Timer
        if (timer.get() != 1) {
            mc.timer.timerSpeed = timer.get();
        }
    }

    @EventTarget
    public void onPreUpdate(EventPreUpdate event) {

        mc.thePlayer.safeWalk = this.safeWalk.get();

        // Getting ItemSlot
        SlotComponent.setSlot(SlotUtil.findBlock(), render.get());

        //Used to detect when to place a block, if over air, allow placement of blocks
        if (PlayerUtils.blockRelativeToPlayer(0, upSideDown.get() ? 2 : -1, 0) instanceof BlockAir) {
            ticksOnAir++;
        } else {
            ticksOnAir = 0;
        }

        this.calculateSneaking();

        // Gets block to place
        targetBlock = PlayerUtils.getPlacePossibility(0, upSideDown.get() ? 3 : 0, 0);

        if (targetBlock == null) {
            return;
        }

        //Gets EnumFacing
        enumFacing = PlayerUtils.getEnumFacing(targetBlock);

        if (enumFacing == null) {
            return;
        }

        final BlockPos position = new BlockPos(targetBlock.xCoord, targetBlock.yCoord, targetBlock.zCoord);

        blockFace = position.add(enumFacing.getOffset().xCoord, enumFacing.getOffset().yCoord, enumFacing.getOffset().zCoord);

        if (blockFace == null || enumFacing == null) {
            return;
        }

        this.calculateRotations();

        if (targetBlock == null || enumFacing == null || blockFace == null) {
            return;
        }

        if ("Auto Jump".equals(this.sameY.get())) {
            mc.gameSettings.keyBindJump.setPressed((mc.thePlayer.onGround && MoveUtil.isMoving()) || mc.gameSettings.keyBindJump.isPressed());
        }

        // Same Y
        final boolean sameY = ((!"Off".equals(this.sameY.get()) || HackSoar.instance.moduleManager.getModule("Speed").getToggled()) && !mc.gameSettings.keyBindJump.isKeyDown()) && MoveUtil.isMoving();

        if (startY - 1 != Math.floor(targetBlock.yCoord) && sameY) {
            return;
        }

        if (mc.thePlayer.inventory.alternativeCurrentItem == SlotComponent.getItemIndex()) {
            if (!BadPacketsComponent.bad(false, true, false, false, true) &&
                    ticksOnAir > MathUtils.getRandom(placeDelayA.get(), placeDelayB.get()) &&
                    (RayCastUtil.overBlock(enumFacing.enumFacing, blockFace, "Strict".equals(rayCast.get())) || "Off".equals(rayCast.get()))) {

                Vec3 hitVec = this.getHitVec();

                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, SlotComponent.getItemStack(), blockFace, enumFacing.enumFacing, hitVec)) {
                    PacketUtil.send(new C0APacketAnimation());
                }

                mc.rightClickDelayTimer = 0;
                ticksOnAir = 0;

                assert SlotComponent.getItemStack() != null;
                if (SlotComponent.getItemStack() != null && SlotComponent.getItemStack().stackSize == 0) {
                    mc.thePlayer.inventory.mainInventory[SlotComponent.getItemIndex()] = null;
                }
            } else if (Math.random() > 0.92 && mc.rightClickDelayTimer <= 0) {
//                ChatUtil.display("Drag: " + Math.random());
                PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
                mc.rightClickDelayTimer = 0;
            }
        }

        //For Same Y
        if (mc.thePlayer.onGround || (mc.gameSettings.keyBindJump.isKeyDown() && !MoveUtil.isMoving())) {
            startY = Math.floor(mc.thePlayer.posY);
        }

        if (mc.thePlayer.posY < startY) {
            startY = mc.thePlayer.posY;
        }
    }

    @EventTarget
    public void onMove(EventMoveInput event) {
        // sprint
        if ("Matrix".equals(sprint.get())) {
            event.setSneakSlowDownMultiplier(0.5);
            mc.gameSettings.keyBindSprint.setPressed(false);
            mc.thePlayer.setSprinting(false);
        }

        forward = event.getForward();
        strafe = event.getStrafe();

        if (!this.sneak.get()) {
            return;
        }

        double speed = this.sneakingSpeed.get().doubleValue();

        if (speed <= 0.2) {
            return;
        }

        event.setSneakSlowDownMultiplier(speed);

        calculateSneaking();
    }

    @EventTarget
    public void onStrafe(EventStrafe event) {
        this.runMode();

        if (!Objects.equals(yawOffset.get(), "0") && !movementCorrection.get()) {
            MoveUtil.useDiagonalSpeed();
        }

        // towers
        switch (tower.get()) {
            case "Watchdog": {
                if (!mc.gameSettings.keyBindJump.isKeyDown() || !MoveUtil.isMoving()) {
                    return;
                }

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = MoveUtil.jumpMotion();
                    mc.thePlayer.motionX *= .65;
                    mc.thePlayer.motionZ *= .65;
                }
                break;
            }
            case "Legit": {
                if (!mc.gameSettings.keyBindJump.isKeyDown()) {
                    return;
                }

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    public void runMode() {
        if ("Telly".equals(this.mode.get())) {
            if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
                mc.thePlayer.jump();
            }
        }
    }

    public void calculateSneaking() {
        mc.gameSettings.keyBindSneak.setPressed(false);

        if (!MoveUtil.isMoving()) {
            return;
        }

        this.sneakingTicks--;

        if (sneakingTicks < 0) {
            incrementedPlacements = false;
        }

        if (!this.sneak.get()) {
            return;
        }

        int ahead = (int) MathUtils.getRandom(startSneakingA.get(), startSneakingB.get());
        int place = (int) MathUtils.getRandom(placeDelayA.get(), placeDelayB.get());
        int after = (int) MathUtils.getRandom(stopSneakingA.get(), stopSneakingB.get());

        if (this.ticksOnAir > 0) {
            this.sneakingTicks = (int) (Math.ceil((after + (place - this.ticksOnAir)) / this.sneakingSpeed.get().doubleValue()));
        }

        if (this.sneakingTicks >= 0 || (ahead == 5 && after == 5)) {
            if (placements % sneakEvery.get() == 0) {
                mc.gameSettings.keyBindSneak.setPressed(true);
            }

            if (!incrementedPlacements) {
                placements++;
            }
            incrementedPlacements = true;
            return;
        }

        if (ahead == 0 && place == 0 && this.ticksOnAir > 0) {
            this.sneakingTicks = 1;
            return;
        }

        if (PlayerUtils.blockRelativeToPlayer(mc.thePlayer.motionX * ahead * sneakingSpeed.get().doubleValue(), MoveUtil.HEAD_HITTER_MOTION, mc.thePlayer.motionZ * ahead * sneakingSpeed.get().doubleValue()) instanceof BlockAir) {
            this.sneakingTicks = (int) Math.floor((5 + place + after) / this.sneakingSpeed.get().doubleValue());
            placements++;
        }
    }

    public void calculateRotations() {
        float yawOffset = Float.parseFloat(String.valueOf(this.yawOffset.get()));

        /* Calculating target rotations */
        switch (mode.get()) {
            case "Normal":
                if (ticksOnAir > 0 && !RayCastUtil.overBlock(RotationComponent.rotations, enumFacing.enumFacing, blockFace, "Strict".equals(rayCast.get()))) {
                    getRotations(Float.parseFloat(String.valueOf(this.yawOffset.get())));
                }
                break;

            case "UPDATED-NCP":

                if (ticksOnAir > 0 && !RayCastUtil.overBlock(RotationComponent.rotations, enumFacing.enumFacing, blockFace, "Strict".equals(rayCast.get()))) {
                    getRotations(Float.parseFloat(String.valueOf(this.yawOffset.get())));
                }

                targetPitch = 69;
                break;

            case "Snap":
                getRotations(yawOffset);

                if (!(ticksOnAir > 0 && !RayCastUtil.overBlock(RotationComponent.rotations, enumFacing.enumFacing, blockFace, "Strict".equals(rayCast.get())))) {
                    targetYaw = (float) (Math.toDegrees(MoveUtil.direction(mc.thePlayer.rotationYaw, forward, strafe))) + yawOffset;
                }
                break;

            case "Telly":
                if (mc.thePlayer.offGroundTicks >= 3) {
                    if (!RayCastUtil.overBlock(RotationComponent.rotations, enumFacing.enumFacing, blockFace, "Strict".equals(rayCast.get()))) {
                        getRotations(yawOffset);
                    }
                } else {
                    getRotations(Float.parseFloat(String.valueOf(this.yawOffset.get())));
                    targetYaw = mc.thePlayer.rotationYaw - yawOffset;
                }
                break;
        }

        /* Randomising slightly */
//        if (Math.random() > 0.8 && targetPitch > 50) {
//            final Vector2f random = new Vector2f((float) (Math.random() - 0.5), (float) (Math.random() / 2));
//
//            if (ticksOnAir <= 0 || RayCastUtil.overBlock(new Vector2f(targetYaw + random.x, targetPitch + random.y), enumFacing.enumFacing,
//                    blockFace, rayCast.get().equals("Strict"))) {
//
//                targetYaw += random.x;
//                targetPitch += random.y;
//            }
//        }

        /* Smoothing rotations */
        final double minRotationSpeed = this.rotationSpeedA.get().doubleValue();
        final double maxRotationSpeed = this.rotationSpeedB.get().doubleValue();
        float rotationSpeed = (float) MathUtils.getRandom(minRotationSpeed, maxRotationSpeed);

        if (rotationSpeed != 0) {
            RotationComponent.setRotations(new Vector2f(targetYaw, targetPitch), rotationSpeed, movementCorrection.get() ? MovementFix.NORMAL : MovementFix.OFF);
        }
    }

    public void getRotations(final float yawOffset) {
        boolean found = false;
        for (float possibleYaw = mc.thePlayer.rotationYaw - 180 + yawOffset; possibleYaw <= mc.thePlayer.rotationYaw + 360 - 180 && !found; possibleYaw += 45) {
            for (float possiblePitch = 90; possiblePitch > 30 && !found; possiblePitch -= possiblePitch > (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 60 : 80) ? 1 : 10) {
                if (RayCastUtil.overBlock(new Vector2f(possibleYaw, possiblePitch), enumFacing.enumFacing, blockFace, true)) {
                    targetYaw = possibleYaw;
                    targetPitch = possiblePitch;
                    found = true;
                }
            }
        }

        if (!found) {
            final Vector2f rotations = RotationUtil.calculate(
                    new Vector3d(blockFace.getX(), blockFace.getY(), blockFace.getZ()), enumFacing.enumFacing);

            targetYaw = rotations.x;
            targetPitch = rotations.y;
        }
    }

    public Vec3 getHitVec() {
        /* Correct HitVec */
        Vec3 hitVec = new Vec3(blockFace.getX() + Math.random(), blockFace.getY() + Math.random(), blockFace.getZ() + Math.random());

        final MovingObjectPosition movingObjectPosition = RayCastUtil.rayCast(RotationComponent.rotations, mc.playerController.getBlockReachDistance());

        switch (enumFacing.enumFacing) {
            case DOWN:
                hitVec.yCoord = blockFace.getY();
                break;

            case UP:
                hitVec.yCoord = blockFace.getY() + 1;
                break;

            case NORTH:
                hitVec.zCoord = blockFace.getZ();
                break;

            case EAST:
                hitVec.xCoord = blockFace.getX() + 1;
                break;

            case SOUTH:
                hitVec.zCoord = blockFace.getZ() + 1;
                break;

            case WEST:
                hitVec.xCoord = blockFace.getX();
                break;
        }

        if (movingObjectPosition != null && movingObjectPosition.getBlockPos().equals(blockFace) &&
                movingObjectPosition.sideHit == enumFacing.enumFacing) {
            hitVec = movingObjectPosition.hitVec;
        }

        return hitVec;
    }
}