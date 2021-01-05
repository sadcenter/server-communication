package xyz.sadcenter.redis.async.impl.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import xyz.sadcenter.redis.async.AsyncRunner;

/**
 * @author sadcenter on 05.01.2021
 * @project RedisCommunication
 */

public final class BukkitSchedulerAsyncRunner implements AsyncRunner {

    private final Plugin plugin;

    public BukkitSchedulerAsyncRunner(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

}
