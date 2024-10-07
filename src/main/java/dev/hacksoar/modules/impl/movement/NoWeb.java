package dev.hacksoar.modules.impl.movement;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventUpdate;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.ListValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.player.PacketUtil;
import dev.hacksoar.utils.world.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.List;
import java.util.Map;

@ModuleTag
public class NoWeb extends Module {
    private final ListValue mode = new ListValue("Mode", new String[]{"Vanilla", "Grim"}, "Vanilla");

    public NoWeb() {
        super("NoWeb", "no web's slowdown", ModuleCategory.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!mc.thePlayer.isInWeb) return;

        switch (mode.get()) {
            case "Vanilla":
                mc.thePlayer.isInWeb = false;
                break;
            case "Grim":
                Map<BlockPos, Block> searchBlock = BlockUtil.searchBlocks(2);
                for (Map.Entry<BlockPos, Block> block : searchBlock.entrySet()) {
                    if (mc.theWorld.getBlockState(block.getKey()).getBlock() instanceof BlockWeb) {
                        PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, block.getKey(), EnumFacing.DOWN));
                        PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block.getKey(), EnumFacing.DOWN));
                    }
                }
                mc.thePlayer.isInWeb = false;
                break;
        }
    }
}
