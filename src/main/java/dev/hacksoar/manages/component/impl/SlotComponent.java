package dev.hacksoar.manages.component.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventPreUpdate;
import dev.hacksoar.api.events.impl.EventRender2D;
import dev.hacksoar.api.events.impl.EventSyncCurrentItem;
import dev.hacksoar.manages.component.Component;
import dev.hacksoar.utils.animation.Animation;
import dev.hacksoar.utils.animation.Easing;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * @author Liycxc
 */
public class SlotComponent extends Component {

    private static final Animation ANIMATION = new Animation(Easing.EASE_OUT_ELASTIC, 250);
    private static boolean render;

    public static void setSlot(final int slot, final boolean render) {
        if (slot < 0 || slot > 8) {
            return;
        }

        mc.thePlayer.inventory.alternativeCurrentItem = slot;
        mc.thePlayer.inventory.alternativeSlot = true;
        SlotComponent.render = render;
    }

    public static void setSlot(final int slot) {
        setSlot(slot, true);
    }

    public static int getItemIndex() {
        final InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
        return inventoryPlayer.alternativeSlot || !ANIMATION.isFinished() ? inventoryPlayer.alternativeCurrentItem : inventoryPlayer.currentItem;
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        final ScaledResolution scaledResolution = event.getScaledResolution();
        final double destinationY = render && mc.thePlayer.inventory.alternativeSlot &&
                (mc.thePlayer.inventory.alternativeCurrentItem != mc.thePlayer.inventory.currentItem || getItemStack() == null || getItemStack().getItem() instanceof ItemBlock) &&
                mc.currentScreen == null ? scaledResolution.getScaledHeight() - 90 : scaledResolution.getScaledHeight();
        ANIMATION.run(destinationY);
        ANIMATION.setDuration(1000);

        if (!render && ANIMATION.isFinished()) {
            return;
        }

        // TODO: Do some render
    }

    @EventTarget
    public void onSyncItem(EventSyncCurrentItem event){
        final InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;

        event.setSlot(inventoryPlayer.alternativeSlot ? inventoryPlayer.alternativeCurrentItem : inventoryPlayer.currentItem);
    }

    public static ItemStack getItemStack() {
        return (mc.thePlayer == null || mc.thePlayer.inventoryContainer == null ? null : mc.thePlayer.inventoryContainer.getSlot(getItemIndex() + 36).getStack());
    }

    @EventTarget
    public void onPreUpdate(EventPreUpdate event) {
        mc.thePlayer.inventory.alternativeSlot = false;
    }
}
