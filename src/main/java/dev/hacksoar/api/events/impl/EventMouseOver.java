package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMouseOver extends Event {
    private double range;
    private float expand;

    public EventMouseOver(double range, float expand) {
        this.range = range;
        this.expand = expand;
    }
}
