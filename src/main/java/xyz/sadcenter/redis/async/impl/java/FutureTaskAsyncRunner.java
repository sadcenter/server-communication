package xyz.sadcenter.redis.async.impl.java;

import xyz.sadcenter.redis.async.AsyncRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author sadcenter on 04.01.2021
 * @project RedisCommunication
 */

public final class FutureTaskAsyncRunner implements AsyncRunner {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void runAsync(Runnable runnable) {
        executorService.execute(runnable);
    }

}
