package dev.hacksoar.modules.impl.utilty;

import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@ModuleTag
public class Teams extends Module {
    private BoolValue scoreboardValue = new BoolValue("ScoreboardTeam", true);
    private BoolValue colorValue = new BoolValue("Color", true);
    private BoolValue gommeSWValue = new BoolValue("GommeSW", false);
    private BoolValue armorValue = new BoolValue("ArmorColor", false);

    public Teams() {
        super("Teams", "We are team", ModuleCategory.Util);
    }

    /**
     * Check if [entity] is in your own team using scoreboard, name color or team prefix
     */
    public boolean isInYourTeam(EntityLivingBase entity) {
        if (mc.thePlayer == null) {
            return false;
        }

        if (scoreboardValue.get() && mc.thePlayer.getTeam() != null && entity.getTeam() != null &&
                mc.thePlayer.getTeam().isSameTeam(entity.getTeam())) {
            return true;
        }
        if (gommeSWValue.get() && mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            String targetName = entity.getDisplayName().getFormattedText().replace("§r", "");
            String clientName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
            if (targetName.startsWith("T") && clientName.startsWith("T")) {
                if (Character.isDigit(targetName.charAt(1)) && Character.isDigit(clientName.charAt(1))) {
                    return targetName.charAt(1) == clientName.charAt(1);
                }
            }
        }
        if (armorValue.get()) {
            EntityPlayer entityPlayer = (EntityPlayer) entity;
            if (mc.thePlayer.inventory.armorInventory[3] != null && entityPlayer.inventory.armorInventory[3] != null) {
                ItemStack myHead = mc.thePlayer.inventory.armorInventory[3];
                ItemArmor myItemArmor = (ItemArmor) myHead.getItem();

                ItemStack entityHead = entityPlayer.inventory.armorInventory[3];
                ItemArmor entityItemArmor = (ItemArmor) entityHead.getItem();

                if (myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead)) {
                    return true;
                }
            }
        }
        if (colorValue.get() && mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            String targetName = entity.getDisplayName().getFormattedText().replace("§r", "");
            String clientName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
            return targetName.startsWith("§" + clientName.charAt(1));
        }

        return false;
    }
}
