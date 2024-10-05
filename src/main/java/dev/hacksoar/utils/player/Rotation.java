package dev.hacksoar.utils.player;

import dev.hacksoar.utils.vector.Vector2f;

public class Rotation extends Vector2f {

    public Rotation(float yaw, float pitch) {
        super(yaw, pitch);
    }

    public float getYaw() {
        return super.getX();
    }

    public float getPitch() {
        return super.getY();
    }
}
