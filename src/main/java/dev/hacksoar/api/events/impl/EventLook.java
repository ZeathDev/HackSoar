package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import dev.hacksoar.utils.vector.Vector2f;
import lombok.Getter;
import lombok.Setter;

public class EventLook extends Event {
    @Getter
    @Setter
    private Vector2f rotation;

    public EventLook(Vector2f rotation) {
        this.rotation = rotation;
    }
}
