package dev.hacksoar.manages.component.impl;

import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.EventPreMotion;
import dev.hacksoar.manages.component.Component;
import dev.hacksoar.utils.player.StopWatch;

/**
 * @author: Liycxc
 * @date: 2023-06-30
 * @time: 19:49
 */
public class SmoothCameraComponent extends Component {

    public static double y;
    public static StopWatch stopWatch = new StopWatch();

    public static void setY(double y) {
        stopWatch.reset();
        SmoothCameraComponent.y = y;
    }

    public static void setY() {
        if (stopWatch.finished(80)) SmoothCameraComponent.y = mc.thePlayer.lastTickPosY;
        stopWatch.reset();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (stopWatch.finished(80)) {
            return;
        }
        mc.thePlayer.cameraYaw = 0;
        mc.thePlayer.cameraPitch = 0;
    };
}