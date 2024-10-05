package dev.hacksoar.modules.impl.utilty;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventSendPacket;
import dev.hacksoar.api.events.impl.EventUpdate;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.BlockPos;

import java.util.Objects;

@ModuleTag
public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool","Fast change you hand's item", ModuleCategory.Util);
    }

    public BoolValue AutoWeapon = new BoolValue("AutoWeapon",true);

    @EventTarget
    public void onAttack(EventSendPacket e)
    {
        if (AutoWeapon.get() && (e.getPacket() instanceof C02PacketUseEntity) && ((C02PacketUseEntity)e.getPacket()).getAction().equals(net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK))
        {
            boolean checks = !mc.thePlayer.isEating();
            if (checks)
                bestSword(((C02PacketUseEntity)e.getPacket()).getEntityFromWorld(mc.theWorld));
        }
    }

    @EventTarget
    public void onClickBlock(EventUpdate e)
    {
        boolean checks = !mc.thePlayer.isEating();
        if(checks && mc.playerController.getIsHittingBlock() && !Objects.isNull(mc.objectMouseOver.getBlockPos()))
            bestTool(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY(), mc.objectMouseOver.getBlockPos().getZ());
    }

    public void bestSword(Entity targetEntity)
    {
        int bestSlot = 0;
        float f = -1F;
        for(int i1 = 36; i1 < 45; i1++)
            if(mc.thePlayer.inventoryContainer.inventorySlots.toArray()[i1] != null && targetEntity != null)
            {
                ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
                if(curSlot != null && (curSlot.getItem() instanceof ItemSword))
                {
                    ItemSword sword = (ItemSword)curSlot.getItem();
                    if(sword.getDamageVsEntity() > f)
                    {
                        bestSlot = i1 - 36;
                        f = sword.getDamageVsEntity();
                    }
                }
            }

        if(f > -1F)
        {
            mc.thePlayer.inventory.currentItem = bestSlot;
            mc.playerController.updateController();
        }
    }

    public void bestTool(int x, int y, int z)
    {
        int blockId = Block.getIdFromBlock(mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
        int bestSlot = 0;
        float f = -1F;
        for(int i1 = 36; i1 < 45; i1++)
            try
            {
                ItemStack curSlot = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
                if(((curSlot.getItem() instanceof ItemTool) || (curSlot.getItem() instanceof ItemSword) || (curSlot.getItem() instanceof ItemShears)) && curSlot.getStrVsBlock(Block.getBlockById(blockId)) > f)
                {
                    bestSlot = i1 - 36;
                    f = curSlot.getStrVsBlock(Block.getBlockById(blockId));
                }
            }
            catch(Exception exception) { }

        if(f != -1F)
        {
            mc.thePlayer.inventory.currentItem = bestSlot;
            mc.playerController.updateController();
        }
    }
}