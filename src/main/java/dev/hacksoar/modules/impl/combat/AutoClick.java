package dev.hacksoar.modules.impl.combat;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventTick;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.IntValue;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.TimerUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemSword;

@ModuleTag
public class AutoClick extends Module {
    public AutoClick() {
        super("AutoClick","Mouse down auto click", ModuleCategory.Combat);
    }
    
    private IntValue aCps = new IntValue("A CPS",8,1,20);
    private IntValue bCps = new IntValue("B CPS",8,1,20);
    private BoolValue leftClick = new BoolValue("Left Click",true);
    private ListValue autoBlock = new ListValue("AutoBlock",new String[]{"None", "Legit", "Fast"},"None",() -> leftClick.get());
    private IntValue autoblockDelay = new IntValue("AutoBlock Delay",120,100,300,() -> leftClick.get() && autoBlock.get().equals("Legit"));

    private long leftDelay = TimerUtils.randomClickDelay(minCps(),maxCps());
    private long leftLastSwing = 0L;

    private TimerUtils timeHelper = new TimerUtils();
    
    @EventTarget
    public void onTick(EventTick tick) {
        // Left Click
        if (mc.gameSettings.keyBindAttack.isKeyDown() && leftClick.get()) {
            if (System.currentTimeMillis() - leftLastSwing >= leftDelay) {
                mc.thePlayer.swingItem();
                KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());

                // Bad packets
               /* switch (mc.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                        break;

                    case BLOCK:
                        BlockPos blockpos = mc.objectMouseOver.getBlockPos();

                        if (mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            mc.playerController.clickBlock(blockpos, mc.objectMouseOver.sideHit);
                            break;
                        }

                    case MISS:
                    default:
                        if (mc.playerController.isNotCreative()) {
                            mc.leftClickCounter = 10;
                        }
                }*/

                // Legit AutoBlock
                if (autoBlock.get().equals("Legit") && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit.isEntityAlive() && mc.thePlayer.inventory.getCurrentItem() != null) {
                    if (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && timeHelper.delay(autoblockDelay.get().longValue())) {
                        mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                        timeHelper.reset();
                    }
                }
                // Fast AutoBlock
                if (autoBlock.get().equals("Fast")) {
                    mc.thePlayer.getCurrentEquippedItem();
                    if (!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)) {
                        return;
                    }
                    mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                }
                leftLastSwing = System.currentTimeMillis();
                leftDelay = TimerUtils.randomClickDelay(minCps(), maxCps());
            }
        }
    }
    
    private int maxCps() {
        return Math.max(aCps.get(),bCps.get());
    }
    
    private int minCps() {
        return Math.min(aCps.get(),bCps.get());
    }
}
