package dev.hacksoar.modules.impl.render;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventCameraRotation;
import dev.hacksoar.api.events.impl.EventScrollMouse;
import dev.hacksoar.api.tags.ModuleTag;
import dev.hacksoar.api.value.impl.FloatValue;
import dev.hacksoar.modules.Module;
import dev.hacksoar.modules.ModuleCategory;
import dev.hacksoar.utils.animation.simple.SimpleAnimation;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

@ModuleTag
public class FarCamera extends Module {

    private final SimpleAnimation animation = new SimpleAnimation(0.0F);
    private float currentFactor = 1.0F;

    private final FloatValue rangeOption = new FloatValue("Range", 20F, 1F, 50F);

    public FarCamera() {
        super("FarCamera", "Move the camera faraway", ModuleCategory.Render);
    }

    @EventTarget
    public void onCameraRotation(EventCameraRotation event) {
        animation.setAnimation(currentFactor, 20F);
        rangeOption.set(animation.getValue());

        float range = rangeOption.get();

        if(mc.gameSettings.thirdPersonView == 1) {
            GlStateManager.translate(0.0F, 0.0F, -range);
        }
    }

    @EventTarget
    public void onScrollMouse(EventScrollMouse event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
            event.setCancelled(true);
            if(event.getAmount() < 0) {
                if(currentFactor < 50) {
                    currentFactor += 8.0F;
                }
            } else if(event.getAmount() > 0) {
                if(currentFactor > 1) {
                    currentFactor -= 6.0F;
                }
            }
        }
    }
}
