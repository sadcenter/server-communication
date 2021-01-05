package xyz.sadcenter.redis.async.impl.java;

import xyz.sadcenter.redis.async.AsyncRunner;

import java.util.concurrent.ForkJoinPool;

/**
 * @author sadcenter on 05.01.2021
 * @project RedisCommunication
 */

public final class ForkJoinPoolAsyncRunner implements AsyncRunner {

    @Override
    public void runAsync(Runnable runnable) {
        ForkJoinPool.commonPool().submit(runnable);
    }

}
