package dev.hacksoar.modules.impl.utilty;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventTick;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.IntValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.TimerUtils;

@ModuleTag
public class MemoryFix extends Module {
    private final BoolValue autoGC = new BoolValue("AutoGC", true);
    private final IntValue gcPercent = new IntValue("GC Limit %", 60, 10, 100);
    public static final BoolValue fastLoadFix = new BoolValue("FastLoad Fix", true);

    private final TimerUtils timer = new TimerUtils();

    public MemoryFix() {
        super("MemoryFix", "fix memory leak", ModuleCategory.Util);
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (autoGC.get()) {
            long maxMemory = Runtime.getRuntime().maxMemory();
            long totalMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            long usedMemory = totalMemory - freeMemory;
            float pct = usedMemory * 100f / maxMemory;

            if (timer.delay(1000, true) && gcPercent.getValue() <= pct) {
                System.gc();
            }
        }
    }
}
