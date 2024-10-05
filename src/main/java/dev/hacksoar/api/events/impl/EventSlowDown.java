package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventSlowDown extends Event {
    private float strafeMultiplier;
    private float forwardMultiplier;
    public EventSlowDown(float strafeMultiplier,float forwardMultiplier) {
        this.strafeMultiplier = strafeMultiplier;
        this.forwardMultiplier = forwardMultiplier;
    }
}
