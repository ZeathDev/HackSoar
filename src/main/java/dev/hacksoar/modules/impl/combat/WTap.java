package dev.hacksoar.modules.impl.combat;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventAttackEntity;
import dev.hacksoar.api.events.impl.EventUpdate;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@ModuleTag
public class WTap extends Module {
    private final BoolValue smart = new BoolValue("Smart", true);

    private EntityLivingBase target = null;
    private int ticks = 0;

    public WTap() {
        super("WTap", "auto combo target", ModuleCategory.Combat);
    }

    @Override
    public void onDisable() {
        target = null;
        ticks = 0;
        super.onDisable();
    }

    @EventTarget
    public void onAttack(EventAttackEntity event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            target = (EntityLivingBase) event.getEntity();
            ticks = 2;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (target == null) return;

        double diffX = mc.thePlayer.posX - target.posX;
        double diffZ = mc.thePlayer.posZ - target.posZ;
        double calcYaw = MathHelper.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0;
        double diffYaw = MathHelper.wrapAngleTo180_float((float) (calcYaw - target.rotationYawHead));

        if (!smart.get() || diffYaw <= 120.0f) {
            if (ticks == 2) {
                mc.gameSettings.keyBindForward.setPressed(false);
                ticks = 1;
            } else if (ticks == 1) {
                mc.gameSettings.keyBindForward.setPressed(GameSettings.isKeyDown(mc.gameSettings.keyBindForward));
                ticks = 0;
            }
        }
    }
}
