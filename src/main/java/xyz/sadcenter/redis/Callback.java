package xyz.sadcenter.redis;

import xyz.sadcenter.redis.abstracts.Packet;

/**
 * @author sadcenter on 27.10.2020
 * @project RedisCommunicationForked
 */

public interface Callback<T extends Packet> {

    void done(T response);

    default void error(String errorMessage) {
    }

    default void timeout() {
    }

}
