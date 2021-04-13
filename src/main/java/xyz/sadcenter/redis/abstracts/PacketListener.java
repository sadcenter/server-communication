package xyz.sadcenter.redis.abstracts;

import org.redisson.api.listener.MessageListener;
import xyz.sadcenter.redis.serializers.RedisSerializer;

/**
 * @author sadcenter on 06.10.2020
 * @project server-communication
 */

public abstract class PacketListener<T extends Packet> implements MessageListener<byte[]> {

    private final String channel;
    private final RedisSerializer redisSerializer;

    public PacketListener(String channel, RedisSerializer redisSerializer) {
        this.channel = channel;
        this.redisSerializer = redisSerializer;
    }

    public RedisSerializer getRedisSerializer() {
        return redisSerializer;
    }

    protected abstract void onPacketReceived(T received);

    @Override
    public void onMessage(CharSequence charSequence, byte[] bytes) {
        onPacketReceived(redisSerializer.deserialize(bytes));
    }

    public String getChannel() {
        return channel;
    }

}
