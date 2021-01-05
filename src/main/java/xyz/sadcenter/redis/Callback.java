package xyz.sadcenter.redis;

import xyz.sadcenter.redis.abstracts.Packet;

/**
 * @author sadcenter on 27.10.2020
 * @project RedisCommunicationForked
 */

public interface Callback {

    void done(Packet response);

    default void error(String errorMessage) {
    }

    default void timeout() {
    }

}
