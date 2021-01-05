package xyz.sadcenter.redis.async.impl.guava;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import xyz.sadcenter.redis.async.AsyncRunner;

import java.util.concurrent.Executors;

/**
 * @author sadcenter on 04.01.2021
 * @project RedisCommunication
 */

public final class ListenableFutureAsyncRunner implements AsyncRunner {

    private final ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    @Override
    public void runAsync(Runnable runnable) {
        listeningExecutorService.submit(runnable);
    }

}
