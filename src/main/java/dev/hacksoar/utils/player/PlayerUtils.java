package dev.hacksoar.utils.player;

import com.google.common.collect.Multimap;
import dev.hacksoar.HackSoar;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.WorldSettings;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;

@UtilityClass
public class PlayerUtils {
    private final HashMap<Integer, Integer> GOOD_POTIONS = new HashMap<Integer, Integer>() {{
        put(6, 1); // Instant Health
        put(10, 2); // Regeneration
        put(11, 3); // Resistance
        put(21, 4); // Health Boost
        put(22, 5); // Absorption
        put(23, 6); // Saturation
        put(5, 7); // Strength
        put(1, 8); // Speed
        put(12, 9); // Fire Resistance
        put(14, 10); // Invisibility
        put(3, 11); // Haste
        put(13, 12); // Water Breathing
    }};

    /**
     * Gets the block at a position
     *
     * @return block
     */
    public Block block(final double x, final double y, final double z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    /**
     * Gets the block at a position
     *
     * @return block
     */
    public Block block(final BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos).getBlock();
    }

    /**
     * Gets the distance between 2 positions
     *
     * @return distance
     */
    public double distance(final BlockPos pos1, final BlockPos pos2) {
        final double x = pos1.getX() - pos2.getX();
        final double y = pos1.getY() - pos2.getY();
        final double z = pos1.getZ() - pos2.getZ();
        return x * x + y * y + z * z;
    }

    /**
     * Gets the block relative to the player from the offset
     *
     * @return block relative to the player
     */
    public Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
    }

//    public Block blockAheadOfPlayer(final double offsetXZ, final double offsetY) {
//        return blockRelativeToPlayer(-Math.sin(MoveUtil.direction()) * offsetXZ, offsetY, Math.cos(MoveUtil.direction()) * offsetXZ);
//    }

    /**
     * Gets another players' username without any formatting
     *
     * @return players username
     */
//    public String name(final EntityPlayer player) {
//        return player.getCommandSenderName();
//    }

    /**
     * Gets the players' username without any formatting
     *
     * @return players username
     */
//    public String name() {
//        return mc.thePlayer.getCommandSenderName();
//    }

    /**
     * Checks if another players' team is the same as the players' team
     *
     * @return same team
     */
    public boolean sameTeam(final EntityLivingBase player) {
        if (player.getTeam() != null && mc.thePlayer.getTeam() != null) {
            final char c1 = player.getDisplayName().getFormattedText().charAt(1);
            final char c2 = mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }
        return false;
    }

    /**
     * Checks if there is a block under the player
     *
     * @return block under
     */
    public boolean isBlockUnder(final double height) {
        return isBlockUnder(height, true);
    }

    public boolean isBlockUnder(final double height, final boolean boundingBox) {
        if (boundingBox) {
            for (int offset = 0; offset < height; offset += 2) {
                final AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                    return true;
                }
            }
        } else {
            for (int offset = 0; offset < height; offset++) {
                if (blockRelativeToPlayer(0, -offset, 0).isFullBlock()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBlockUnder() {
        return isBlockUnder(mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
    }

    /**
     * Checks if a potion is good
     *
     * @return good potion
     */
    public boolean goodPotion(final int id) {
        return GOOD_POTIONS.containsKey(id);
    }

    /**
     * Gets a potions ranking
     *
     * @return potion ranking
     */
    public int potionRanking(final int id) {
        return GOOD_POTIONS.getOrDefault(id, -1);
    }

    /**
     * Fake damages the player
     */
//    public void fakeDamage() {
//        mc.thePlayer.handleHealthUpdate((byte) 2);
//        mc.ingameGUI.healthUpdateCounter = mc.ingameGUI.updateCounter + 20;
//    }

    /**
     * Checks if the player is near a block
     *
     * @return block near
     */
    public boolean blockNear(final int range) {
        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z) {
                    final Block block = blockRelativeToPlayer(x, y, z);

                    if (!(block instanceof BlockAir)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks if the player is inside a block
     *
     * @return inside block
     */
    public boolean insideBlock() {
        if (mc.thePlayer.ticksExisted < 5) {
            return false;
        }

        final EntityPlayerSP player = mc.thePlayer;
        final WorldClient world = mc.theWorld;
        final AxisAlignedBB bb = player.getEntityBoundingBox();
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(bb.minY); y < MathHelper.floor_double(bb.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
                    final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(world, new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)))) != null && player.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Sends a click to Minecraft legitimately
     */
    public void sendClick(final int button, final boolean state) {
        final int keyBind = button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode();

        KeyBinding.setKeyBindState(keyBind, state);

        if (state) {
            KeyBinding.onTick(keyBind);
        }
    }

    public static boolean onLiquid() {
        boolean onLiquid = false;
        final AxisAlignedBB playerBB = mc.thePlayer.getEntityBoundingBox();
        final WorldClient world = mc.theWorld;
        final int y = (int) playerBB.offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
                final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

//    public EnumFacingOffset getEnumFacing(final Vec3 position) {
//        for (int x2 = -1; x2 <= 1; x2 += 2) {
//            if (!(PlayerUtil.block(position.xCoord + x2, position.yCoord, position.zCoord) instanceof BlockAir)) {
//                if (x2 > 0) {
//                    return new EnumFacingOffset(EnumFacing.WEST, new Vec3(x2, 0, 0));
//                } else {
//                    return new EnumFacingOffset(EnumFacing.EAST, new Vec3(x2, 0, 0));
//                }
//            }
//        }
//
//        for (int y2 = -1; y2 <= 1; y2 += 2) {
//            if (!(PlayerUtil.block(position.xCoord, position.yCoord + y2, position.zCoord) instanceof BlockAir)) {
//                if (y2 < 0) {
//                    return new EnumFacingOffset(EnumFacing.UP, new Vec3(0, y2, 0));
//                }
//            }
//        }
//
//        for (int z2 = -1; z2 <= 1; z2 += 2) {
//            if (!(PlayerUtil.block(position.xCoord, position.yCoord, position.zCoord + z2) instanceof BlockAir)) {
//                if (z2 < 0) {
//                    return new EnumFacingOffset(EnumFacing.SOUTH, new Vec3(0, 0, z2));
//                } else {
//                    return new EnumFacingOffset(EnumFacing.NORTH, new Vec3(0, 0, z2));
//                }
//            }
//        }
//
//        return null;
//    }

    // This methods purpose is to get block placement possibilities, blocks are 1 unit thick so please don't change it to 0.5 it causes bugs.
    public Vec3 getPlacePossibility(double offsetX, double offsetY, double offsetZ) {
        final List<Vec3> possibilities = new ArrayList<>();
        final int range = (int) (5 + (Math.abs(offsetX) + Math.abs(offsetZ)));

        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z) {
                    final Block block = blockRelativeToPlayer(x, y, z);

                    if (!(block instanceof BlockAir)) {
                        for (int x2 = -1; x2 <= 1; x2 += 2)
                            possibilities.add(new Vec3(mc.thePlayer.posX + x + x2, mc.thePlayer.posY + y, mc.thePlayer.posZ + z));

                        for (int y2 = -1; y2 <= 1; y2 += 2)
                            possibilities.add(new Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y + y2, mc.thePlayer.posZ + z));

                        for (int z2 = -1; z2 <= 1; z2 += 2)
                            possibilities.add(new Vec3(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z + z2));
                    }
                }
            }
        }

        possibilities.removeIf(vec3 -> mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord) > 5 || !(block(vec3.xCoord, vec3.yCoord, vec3.zCoord) instanceof BlockAir));

        if (possibilities.isEmpty()) return null;

        possibilities.sort(Comparator.comparingDouble(vec3 -> {

            final double d0 = (mc.thePlayer.posX + offsetX) - vec3.xCoord;
            final double d1 = (mc.thePlayer.posY - 1 + offsetY) - vec3.yCoord;
            final double d2 = (mc.thePlayer.posZ + offsetZ) - vec3.zCoord;
            return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);

        }));

        return possibilities.get(0);
    }

    public Vec3 getPlacePossibility() {
        return getPlacePossibility(0, 0, 0);
    }

	private static final Minecraft mc = Minecraft.getMinecraft();
	
    public static final Map<Integer, Float> MODIFIER_BY_TICK = new HashMap<>();
    
	public static float getSpeed() {
		double distTraveledLastTickX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double distTraveledLastTickZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		double currentSpeed = MathHelper.sqrt_double(distTraveledLastTickX * distTraveledLastTickX
				+ distTraveledLastTickZ * distTraveledLastTickZ);
		
		return (float) (currentSpeed / 0.05);
	}
	
    public static int getPotionsFromInventory() {
        int count = 0;

        for (int i = 1; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();

                if (item instanceof ItemPotion) {
                    ItemPotion potion = (ItemPotion) item;

                    if (potion.getEffects(is) != null) {
                        Iterator<PotionEffect> iterator = potion.getEffects(is).iterator();

                        while (iterator.hasNext()) {
                            Object o = iterator.next();
                            PotionEffect effect = (PotionEffect) o;

                            if (effect.getPotionID() == Potion.heal.id) {
                                ++count;
                            }
                        }
                    }
                }
            }
        }

        return count;
    }
    
    public static boolean isSpectator(){
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR;
    }
    
    public static boolean isCreative(){
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.CREATIVE;
    }
    
    public static boolean isSurvival(){
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SURVIVAL;
    }

    public static boolean isNotMoving() {
        return mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0;
    }

    public static boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }

/*    public static double getDistanceToFall() {
        double distance = 0.0;
        for (double i = mc.thePlayer.posY; i > 0.0; --i) {
            final Block block = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ));
            if (block.getMaterial() != Material.air && block.isBlockNormalCube() && block.isCollidable()) {
                distance = i;
                break;
            }
            if (i < 0.0) {
                break;
            }
        }
        final double distancetofall = mc.thePlayer.posY - distance - 1.0;
        return distancetofall;
    }*/

    public static float[] aimAtLocation(final double x, final double y, final double z, final EnumFacing facing) {
        final EntitySnowball temp = new EntitySnowball(mc.theWorld);
        temp.posX = x + 0.5;
        temp.posY = y + 0.5;
        temp.posZ = z + 0.5;
        final EntitySnowball entitySnowball = temp;
        entitySnowball.posX += facing.getDirectionVec().getX() * 0.25;
        final EntitySnowball entitySnowball2 = temp;
        entitySnowball2.posY += facing.getDirectionVec().getY() * 0.25;
        final EntitySnowball entitySnowball3 = temp;
        entitySnowball3.posZ += facing.getDirectionVec().getZ() * 0.25;
        return aimAtLocation(temp.posX, temp.posY, temp.posZ);
    }

    public static float[] aimAtLocation(final double positionX, final double positionY, final double positionZ) {
        final double x = positionX - mc.thePlayer.posX;
        final double y = positionY - mc.thePlayer.posY;
        final double z = positionZ - mc.thePlayer.posZ;
        final double distance = MathHelper.sqrt_double(x * x + z * z);
        return new float[]{(float) (Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f,
                (float) (-(Math.atan2(y, distance) * 180.0 / 3.141592653589793))};
    }

    public static void tellPlayer(Object string) {
        if (string != null && mc.thePlayer != null)
            mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.WHITE + HackSoar.instance.getName() + EnumChatFormatting.GRAY + "]: " + EnumChatFormatting.GRAY + string));
    }

    private static final ArrayList<String> messages = new ArrayList<>();
    public static void tellPlayerIrc(String string) {
        messages.add(string);
        if (string != null && mc.thePlayer != null) {
            for (String sth : messages) {
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.WHITE + "IRC" + EnumChatFormatting.GRAY + "]: " + EnumChatFormatting.GRAY + sth));
            }
            messages.clear();
        }
    }

    public static void tellPlayerIrcMessage(String name,String message) {
        if (message != null && mc.thePlayer != null)
            mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.WHITE + name + EnumChatFormatting.GRAY + "]: " + EnumChatFormatting.GRAY + message));
    }

    public void portMove(float yaw, float multiplyer, float up) {
        double moveX = -Math.sin(Math.toRadians(yaw)) * (double) multiplyer;
        double moveZ = Math.cos(Math.toRadians(yaw)) * (double) multiplyer;
        double moveY = up;
        mc.thePlayer.setPosition(moveX + mc.thePlayer.posX, moveY + mc.thePlayer.posY,
                moveZ + mc.thePlayer.posZ);
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static float getDirection() {
        float yaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        yaw *= 0.017453292f;
        return yaw;
    }

    public static boolean isInWater() {
        return mc.theWorld.getBlockState(
                        new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ))
                .getBlock().getMaterial() == Material.water;
    }

    public static void toFwd(final double speed) {
        final float yaw = mc.thePlayer.rotationYaw * 0.017453292f;
        final EntityPlayerSP thePlayer = mc.thePlayer;
        thePlayer.motionX -= MathHelper.sin(yaw) * speed;
        final EntityPlayerSP thePlayer2 = mc.thePlayer;
        thePlayer2.motionZ += MathHelper.cos(yaw) * speed;
    }

    public static void setSpeed(final double speed) {
        mc.thePlayer.motionX = -(Math.sin(getDirection()) * speed);
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    public static double getSpeed(Entity entity) {
        return Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
    }

    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer) {
        return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }

    public static Block getBlock(final BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
        return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(final double tpX, final double tpY, final double tpZ,
                                                               final double speed) {
        final ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        final double posX = tpX - mc.thePlayer.posX;
        final double posY = tpY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + 1.1);
        final double posZ = tpZ - mc.thePlayer.posZ;
        final float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
        final float pitch = (float) (-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0
                / 3.141592653589793);
        double tmpX = mc.thePlayer.posX;
        double tmpY = mc.thePlayer.posY;
        double tmpZ = mc.thePlayer.posZ;
        double steps = 1.0;
        for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
                tpZ); d += speed) {
            ++steps;
        }
        for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
                tpZ); d += speed) {
            tmpX = mc.thePlayer.posX - Math.sin(getDirection(yaw)) * d;
            tmpZ = mc.thePlayer.posZ + Math.cos(getDirection(yaw)) * d;
            tmpY -= (mc.thePlayer.posY - tpY) / steps;
            positions.add(new Vector3f((float) tmpX, (float) tmpY, (float) tmpZ));
        }
        positions.add(new Vector3f((float) tpX, (float) tpY, (float) tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        if (mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        yaw *= 0.017453292f;
        return yaw;
    }

    public static double getDistance(final double x1, final double y1, final double z1, final double x2,
                                     final double y2, final double z2) {
        final double d0 = x1 - x2;
        final double d2 = y1 - y2;
        final double d3 = z1 - z2;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }

    public static boolean MovementInput() {
        return mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindLeft.isPressed()
                || mc.gameSettings.keyBindRight.isPressed() || mc.gameSettings.keyBindBack.isPressed();
    }

    public static void blockHit(Entity en, boolean value) {
        ItemStack stack = mc.thePlayer.getCurrentEquippedItem();

        if (mc.thePlayer.getCurrentEquippedItem() != null && en != null && value) {
            if (stack.getItem() instanceof ItemSword && mc.thePlayer.swingProgress > 0.2) {
                mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
            }
        }
    }

    public static float getItemAtkDamage(ItemStack itemStack) {
        final Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty()) {
            final Iterator iterator = multimap.entries().iterator();
            if (iterator.hasNext()) {
                final Map.Entry entry = (Map.Entry) iterator.next();
                final AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
                double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2
                        ? attributeModifier.getAmount()
                        : attributeModifier.getAmount() * 100.0;

                if (attributeModifier.getAmount() > 1.0) {
                    return 1.0f + (float) damage;
                }
                return 1.0f;
            }
        }
        return 1.0f;
    }

    public static int bestWeapon(Entity target) {
        int firstSlot = mc.thePlayer.inventory.currentItem = 0;
        int bestWeapon = -1;
        int j = 1;

        for (byte i = 0; i < 9; i++) {
            mc.thePlayer.inventory.currentItem = i;
            ItemStack itemStack = mc.thePlayer.getHeldItem();

            if (itemStack != null) {
                int itemAtkDamage = (int) getItemAtkDamage(itemStack);
                itemAtkDamage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

                if (itemAtkDamage > j) {
                    j = itemAtkDamage;
                    bestWeapon = i;
                }
            }
        }

        if (bestWeapon != -1) {
            return bestWeapon;
        } else {
            return firstSlot;
        }
    }

    public static void shiftClick(Item i) {
        for (int i1 = 9; i1 < 37; ++i1) {
            ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();

            if (itemstack != null && itemstack.getItem() == i) {
                mc.playerController.windowClick(0, i1, 0, 1, mc.thePlayer);
                break;
            }
        }
    }

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemstack == null) {
                return false;
            }
        }

        return true;
    }

    public static boolean isMoving() {
        if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
            return ((mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }

    public static boolean isMoving2() {
        return ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F));
    }

    public static boolean isAirUnder(Entity ent) {
        return mc.theWorld.getBlockState(new BlockPos(ent.posX, ent.posY - 1, ent.posZ)).getBlock() == Blocks.air;
    }

    public static boolean inLiquid() {
        return mc.thePlayer.isInWater() || mc.thePlayer.isInLava();
    }

    public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        double endX = (double) endPos.getX() + 0.5D;
        double endY = (double) endPos.getY() + 1.0D;
        double endZ = (double) endPos.getZ() + 0.5D;
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);

        for (int count = 0; distance > slack; ++count) {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }

            boolean next = false;
            double diffX = curX - endX;
            double diffY = curY - endY;
            double diffZ = curZ - endZ;
            double offset = (count & 1) == 0 ? pOffset[0] : pOffset[1];
            if (diffX < 0.0D) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                } else {
                    curX += Math.abs(diffX);
                }
            }

            if (diffX > 0.0D) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                } else {
                    curX -= Math.abs(diffX);
                }
            }

            if (diffY < 0.0D) {
                if (Math.abs(diffY) > 0.25D) {
                    curY += 0.25D;
                } else {
                    curY += Math.abs(diffY);
                }
            }

            if (diffY > 0.0D) {
                if (Math.abs(diffY) > 0.25D) {
                    curY -= 0.25D;
                } else {
                    curY -= Math.abs(diffY);
                }
            }

            if (diffZ < 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                } else {
                    curZ += Math.abs(diffZ);
                }
            }

            if (diffZ > 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                } else {
                    curZ -= Math.abs(diffZ);
                }
            }

            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
        }

    }

    public static void disconnectServer(String customMessage) {
        mc.thePlayer.sendQueue.getNetworkManager().closeChannel(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "[Flux] " + EnumChatFormatting.GRAY + customMessage));
    }
    
    static {
        MODIFIER_BY_TICK.put(0, 0.0F);
        MODIFIER_BY_TICK.put(1, 0.00037497282f);
        MODIFIER_BY_TICK.put(2, 0.0015000105f);
        MODIFIER_BY_TICK.put(3, 0.0033749938f);
        MODIFIER_BY_TICK.put(4, 0.0059999824f);
        MODIFIER_BY_TICK.put(5, 0.009374976f);
        MODIFIER_BY_TICK.put(6, 0.013499975f);
        MODIFIER_BY_TICK.put(7, 0.01837498f);
        MODIFIER_BY_TICK.put(8, 0.023999989f);
        MODIFIER_BY_TICK.put(9, 0.030375004f);
        MODIFIER_BY_TICK.put(10, 0.037500024f);
        MODIFIER_BY_TICK.put(11, 0.04537499f);
        MODIFIER_BY_TICK.put(12, 0.05400002f);
        MODIFIER_BY_TICK.put(13, 0.063374996f);
        MODIFIER_BY_TICK.put(14, 0.07349998f);
        MODIFIER_BY_TICK.put(15, 0.084375024f);
        MODIFIER_BY_TICK.put(16, 0.096000016f);
        MODIFIER_BY_TICK.put(17, 0.10837501f);
        MODIFIER_BY_TICK.put(18, 0.121500015f);
        MODIFIER_BY_TICK.put(19, 0.13537502f);
        MODIFIER_BY_TICK.put(20, 0.14999998f);
    }

    public EnumFacingOffset getEnumFacing(final Vec3 position) {
        for (int x2 = -1; x2 <= 1; x2 += 2) {
            if (!(PlayerUtils.block(position.xCoord + x2, position.yCoord, position.zCoord) instanceof BlockAir)) {
                if (x2 > 0) {
                    return new EnumFacingOffset(EnumFacing.WEST, new Vec3(x2, 0, 0));
                } else {
                    return new EnumFacingOffset(EnumFacing.EAST, new Vec3(x2, 0, 0));
                }
            }
        }

        for (int y2 = -1; y2 <= 1; y2 += 2) {
            if (!(PlayerUtils.block(position.xCoord, position.yCoord + y2, position.zCoord) instanceof BlockAir)) {
                if (y2 < 0) {
                    return new EnumFacingOffset(EnumFacing.UP, new Vec3(0, y2, 0));
                }
            }
        }

        for (int z2 = -1; z2 <= 1; z2 += 2) {
            if (!(PlayerUtils.block(position.xCoord, position.yCoord, position.zCoord + z2) instanceof BlockAir)) {
                if (z2 < 0) {
                    return new EnumFacingOffset(EnumFacing.SOUTH, new Vec3(0, 0, z2));
                } else {
                    return new EnumFacingOffset(EnumFacing.NORTH, new Vec3(0, 0, z2));
                }
            }
        }

        return null;
    }
}
