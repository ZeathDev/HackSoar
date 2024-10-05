package dev.hacksoar.utils.player;

import dev.hacksoar.HackSoar;
import lombok.experimental.UtilityClass;
import net.minecraft.network.Packet;

@UtilityClass
public final class PacketUtil {

    public void send(final Packet<?> packet) {
        HackSoar.mc.getNetHandler().addToSendQueue(packet);
    }

    public void sendNoEvent(final Packet<?> packet) {
        HackSoar.mc.getNetHandler().addToSendQueueUnregistered(packet);
    }

    public void queue(final Packet<?> packet) {
        if (isServerPacket(packet)) {
            HackSoar.mc.getNetHandler().addToSendQueue(packet);
        } else {
            HackSoar.mc.getNetHandler().addToSendQueue(packet);
        }
    }

    public void queueNoEvent(final Packet<?> packet) {
        if (isServerPacket(packet)) {
            HackSoar.mc.getNetHandler().addToSendQueueUnregistered(packet);
        } else {
            HackSoar.mc.getNetHandler().addToSendQueueUnregistered(packet);
        }
    }

    public void receive(final Packet<?> packet) {
        HackSoar.mc.getNetHandler().addToSendQueue(packet);
    }

    public void receiveNoEvent(final Packet<?> packet) {
        HackSoar.mc.getNetHandler().addToSendQueueUnregistered(packet);
    }

    private boolean isServerPacket(final Packet<?> packet) {
        return packet.toString().toCharArray()[34] == 'S';
    }

    private boolean isClientPacket(final Packet<?> packet) {
        return packet.toString().toCharArray()[34] == 'C';
    }

    public static class TimedPacket {
        private final Packet<?> packet;
        private final long time;

        public TimedPacket(final Packet<?> packet, final long time) {
            this.packet = packet;
            this.time = time;
        }

        public Packet<?> getPacket() {
            return packet;
        }

        public long getTime() {
            return time;
        }
    }
}
