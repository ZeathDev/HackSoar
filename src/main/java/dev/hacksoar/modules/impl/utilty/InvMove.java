package dev.hacksoar.modules.impl.utilty;

import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;

@ModuleTag
public class InvMove extends Module {
    public InvMove() {
        super("InvMove","You can move when you open gui", ModuleCategory.Util);
    }
}
