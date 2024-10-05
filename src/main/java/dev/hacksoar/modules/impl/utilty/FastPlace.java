package dev.hacksoar.modules.impl.utilty;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventPreMotion;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.IntValue;
import dev.hacksoar.manages.component.impl.SlotComponent;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import net.minecraft.item.ItemBlock;

@ModuleTag
public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace","FastPlace", ModuleCategory.Util);
    }

    private final IntValue delay = new IntValue("Delay", 0, 0, 3);

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (SlotComponent.getItemStack() != null && SlotComponent.getItemStack().getItem() instanceof ItemBlock) {
            mc.rightClickDelayTimer = Math.min(mc.rightClickDelayTimer, this.delay.get());
        }
    }
}
