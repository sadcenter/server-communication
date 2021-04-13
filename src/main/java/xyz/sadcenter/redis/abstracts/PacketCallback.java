package xyz.sadcenter.redis.abstracts;

import java.util.UUID;

/**
 * @author sadcenter on 27.10.2020
 * @project RedisCommunicationForked
 */
public abstract class PacketCallback extends Packet {

    private final UUID packetID;
    public PacketCallback() {
        this.packetID = UUID.randomUUID();
    }

    public UUID getPacketID() {
        return packetID;
    }
}
