package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMove extends Event {
    private double posX, posY, posZ;
    public EventMove(double posX,double posY, double posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }
}
