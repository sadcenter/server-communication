package xyz.sadcenter.redis.async;

import xyz.sadcenter.redis.abstracts.Packet;
import xyz.sadcenter.redis.abstracts.PacketListener;
import xyz.sadcenter.redis.serializers.RedisSerializer;

/**
 * @author sadcenter on 04.01.2021
 * @project Sectors
 */

public abstract class AsyncPacketListener<T extends Packet> extends PacketListener<T> {

    private final AsyncRunner asyncRunner;

    public AsyncPacketListener(String channel, RedisSerializer redisSerializer, AsyncRunner asyncRunner) {
        super(channel, redisSerializer);
        this.asyncRunner = asyncRunner;
    }

    @Override
    public void onMessage(CharSequence charSequence, byte[] bytes) {
        asyncRunner.runAsync(() -> onPacketReceived(super.getRedisSerializer().deserialize(bytes)));
    }
}
