package dev.hacksoar.modules.impl.movement;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventPreMotion;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.manages.component.impl.SlotComponent;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import net.minecraft.item.ItemBlock;
import org.lwjgl.input.Keyboard;

/**
 * @author Liycxc
 */
@ModuleTag
public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", "Safe Walk", ModuleCategory.Movement, Keyboard.KEY_X);
    }

    private final BoolValue blocksOnly = new BoolValue("Blocks Only", false);
    private final BoolValue backwardsOnly = new BoolValue("Backwards Only", false);

    @EventTarget
    public void onPreUpdate(EventPreMotion event){

        mc.thePlayer.safeWalk = mc.thePlayer.onGround && (!mc.gameSettings.keyBindForward.isKeyDown() || !backwardsOnly.getValue()) &&
                ((SlotComponent.getItemStack() != null && SlotComponent.getItemStack().getItem() instanceof ItemBlock) ||
                        !this.blocksOnly.getValue());
    };
}
