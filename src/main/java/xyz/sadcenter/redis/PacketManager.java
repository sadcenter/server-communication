package xyz.sadcenter.redis;


import com.google.common.cache.*;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RedissonClient;
import xyz.sadcenter.redis.abstracts.Packet;
import xyz.sadcenter.redis.abstracts.PacketCallback;
import xyz.sadcenter.redis.abstracts.PacketListener;
import xyz.sadcenter.redis.serializers.RedisSerializer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author sadcenter on 06.10.2020
 * @project RedisCommunicationForked
 */

public class PacketManager {

    private final RedissonClient redissonClient;
    private final RedisSerializer redisSerialization;
    private final String tempChannelName;
    private final Map<String, PacketListener<? extends Packet>> packetListenerCache = new ConcurrentHashMap<>();
    private final Cache<UUID, Callback> tempListeners;

    public PacketManager(RedissonClient redissonClient, RedisSerializer redisSerializer, int timeoutSeconds) {
        this(redissonClient, redisSerializer, timeoutSeconds, "temps");
    }

    public PacketManager(RedissonClient redissonClient, RedisSerializer redisSerializer) {
        this(redissonClient, redisSerializer, 20, "temps");
    }

    public PacketManager(RedissonClient redissonClient, RedisSerializer redisSerializer, String tempsChannel) {
        this(redissonClient, redisSerializer, 20, tempsChannel);
    }

    public PacketManager(RedissonClient redissonClient, RedisSerializer redisSerialization, int timeoutSeconds, String tempsChannel) {
        this.redissonClient = redissonClient;
        this.redisSerialization = redisSerialization;
        this.tempListeners = CacheBuilder.newBuilder().expireAfterWrite(timeoutSeconds, TimeUnit.SECONDS).removalListener(new RemovalListener<UUID, Callback>() {
            @Override
            public void onRemoval(@NotNull RemovalNotification<UUID, Callback> removalNotification) {
                if (removalNotification.getValue() == null)
                    return;

                if (removalNotification.getCause().equals(RemovalCause.EXPIRED)) {
                    removalNotification.getValue().timeout();
                    System.out.println("[INFO] Callback " + removalNotification.getKey() + " didn't receive response from any server");
                }
            }

        }).build();
        this.tempChannelName = tempsChannel;

        this.redissonClient.getTopic(tempsChannel)
                .addListener(byte[].class, (charSequence, bytes) -> {
                    PacketCallback response = this.redisSerialization.deserialize(bytes);
                    Callback callback = this.tempListeners.getIfPresent(response.getPacketID());
                    if (callback == null) {
                        return;
                    }

                    this.tempListeners.invalidate(response.getPacketID());

                    if (response.isSuccess())
                        callback.done(response);
                    else
                        callback.error(response.getErrorMessage());

                });
    }


    public void sendPacket(String channel, Packet packet) {
        this.redissonClient.getTopic(channel).publish(redisSerialization.serialize(packet));
    }

    public void reply(PacketCallback packet) {
        this.sendPacket(this.tempChannelName, packet);
    }

    public void sendPacket(String channel, PacketCallback packetCallback, Callback callback) {
        this.redissonClient.getTopic(channel).publish(this.redisSerialization.serialize(packetCallback));
        this.tempListeners.put(packetCallback.getPacketID(), callback);
    }

    public void registerPacketListener(PacketListener<? extends Packet> packetListener) {
        this.redissonClient.getTopic(packetListener.getChannel()).addListener(byte[].class, packetListener);
    }

    public void registerAsyncPacketListener(PacketListener<? extends Packet> packetListener) {
        this.redissonClient.getTopic(packetListener.getChannel()).addListenerAsync(byte[].class, packetListener);
    }

    public RedisSerializer getRedisSerialization() {
        return redisSerialization;
    }

    public Map<String, PacketListener<? extends Packet>> getPacketListenerCache() {
        return packetListenerCache;
    }

    public Cache<UUID, Callback> getTempListeners() {
        return tempListeners;
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}

