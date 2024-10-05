package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import lombok.Getter;
import lombok.Setter;

public class EventStrafe extends Event {
    @Setter
    @Getter
    public float strafe,forward,friction,yaw;
    public EventStrafe(float strafe,float forward,float friction,float yaw) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
    }
}
