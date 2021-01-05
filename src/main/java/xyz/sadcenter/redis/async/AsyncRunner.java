package xyz.sadcenter.redis.async;

/**
 * @author sadcenter on 04.01.2021
 * @project Sectors
 */

public interface AsyncRunner {

    void runAsync(Runnable runnable);

}
