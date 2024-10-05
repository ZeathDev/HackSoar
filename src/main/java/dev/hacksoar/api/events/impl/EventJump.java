package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventJump extends Event {
    private float jumpMotion;
    private float yaw;

    public EventJump(float jumpMotion, float movementYaw) {
        this.jumpMotion = jumpMotion;
        this.yaw = movementYaw;
    }
}
