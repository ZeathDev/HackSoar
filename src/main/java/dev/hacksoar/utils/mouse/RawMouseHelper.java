package dev.hacksoar.utils.mouse;

import dev.hacksoar.pvp.management.mods.impl.RawInputMod;
import net.minecraft.util.MouseHelper;

public class RawMouseHelper extends MouseHelper{
	
    @Override
    public void mouseXYChange() {
        this.deltaX = RawInputMod.dx;
        RawInputMod.dx = 0;
        this.deltaY = -RawInputMod.dy;
        RawInputMod.dy = 0;
    }
}
