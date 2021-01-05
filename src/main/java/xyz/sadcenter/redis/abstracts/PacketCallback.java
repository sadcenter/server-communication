package xyz.sadcenter.redis.abstracts;

import java.util.UUID;

/**
 * @author sadcenter on 27.10.2020
 * @project RedisCommunicationForked
 */
public abstract class PacketCallback extends Packet {

    private final UUID packetID;
    private String errorMessage;

    public PacketCallback() {
        this.packetID = UUID.randomUUID();
    }

    public UUID getPacketID() {
        return packetID;
    }

    public boolean isSuccess() {
        return errorMessage == null;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
