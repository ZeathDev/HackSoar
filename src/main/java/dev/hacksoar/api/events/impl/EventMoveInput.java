package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMoveInput extends Event {
    private float forward, strafe;
    private boolean jump, sneak;
    private double sneakSlowDownMultiplier;

    public EventMoveInput(float moveForward, float moveStrafe, boolean jump, boolean sneak, double v) {
        this.forward = moveForward;
        this.strafe = moveStrafe;
        this.jump = jump;
        this.sneak = sneak;
        this.sneakSlowDownMultiplier = v;
    }
}
