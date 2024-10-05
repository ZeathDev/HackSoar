package dev.hacksoar.modules.impl.render;

import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;

@ModuleTag
public class NoHurtCam extends Module {
    public NoHurtCam() {
        super("NoHurtCam", "removes shaking after being hit", ModuleCategory.Render);
    }
}
