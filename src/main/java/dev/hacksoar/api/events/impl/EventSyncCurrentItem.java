package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: Liycxc
 * @date: 2023-06-30
 * @time: 20:03
 */
@Setter
@Getter
public class EventSyncCurrentItem extends Event {
    private int slot;
    public EventSyncCurrentItem(int slot) {
        this.slot = slot;
    }
}