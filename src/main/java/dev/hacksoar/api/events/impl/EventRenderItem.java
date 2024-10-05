package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

@Getter
@Setter
public class EventRenderItem extends Event {
    private EnumAction enumAction;
    private boolean useItem;
    private float animationProgression, partialTicks, swingProgress;
    private ItemStack itemToRender;
    public EventRenderItem(EnumAction enumAction,boolean useItem,float animationProgression,float partialTicks,float swingProgress,ItemStack itemToRender) {
        this.enumAction = enumAction;
        this.useItem = useItem;
        this.animationProgression = animationProgression;
        this.partialTicks = partialTicks;
        this.swingProgress = swingProgress;
        this.itemToRender = itemToRender;
    }
}
