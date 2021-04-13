package xyz.sadcenter.redis.serializers;

import xyz.sadcenter.redis.abstracts.Packet;

/**
 * @author sadcenter on 04.01.2021
 * @project server-communication
 */

public interface RedisSerializer {

    byte[] serialize(Packet packet);

    <T extends Packet> T deserialize(byte[] packet);

}
