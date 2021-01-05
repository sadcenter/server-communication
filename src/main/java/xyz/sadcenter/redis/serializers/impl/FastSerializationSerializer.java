package xyz.sadcenter.redis.serializers.impl;

import org.nustaq.serialization.FSTConfiguration;
import xyz.sadcenter.redis.abstracts.Packet;
import xyz.sadcenter.redis.serializers.RedisSerializer;

import java.nio.ByteBuffer;

/**
 * @author sadcenter on 04.01.2021
 * @project RedisCommunication
 */

public final class FastSerializationSerializer implements RedisSerializer {

    private final FSTConfiguration fstConfiguration = FSTConfiguration.createDefaultConfiguration();

    @Override
    public byte[] serialize(Packet packet) {
        return ByteBuffer.wrap(fstConfiguration.asByteArray(packet)).array();
    }

    @Override
    public <T extends Packet> T deserialize(byte[] bytes) {
        ByteBuffer packet = ByteBuffer.wrap(bytes);
        byte[] buffer = new byte[packet.remaining()];
        packet.get(buffer);
        return (T) fstConfiguration.asObject(buffer);
    }
}
