package com.elliegabel.s.executor;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Executor service utility.
 */
public final class Executor {

    private static final int THREAD_POOL_SIZE = 5;
    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);

    @NotNull
    public static ScheduledExecutorService getExecutor() {
        return EXECUTOR;
    }

    /**
     * Execute a runnable now.
     *
     * @param runnable Runnable to execute.
     */
    public static void execute(@NotNull Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    /**
     * Execute a runnable later.
     *
     * @param runnable Runnable to execute.
     * @param delay Delay before the task is run.
     * @param timeUnit TimeUnit of the delay.
     * @return The future task.
     */
    @NotNull
    public static ScheduledFuture<?> executeLater(@NotNull Runnable runnable, long delay, @NotNull TimeUnit timeUnit) {
        return EXECUTOR.schedule(runnable, delay, timeUnit);
    }

    /**
     * Schedule a runnable to be executed at a fixed interval.
     *
     * @param runnable Runnable to execute.
     * @param delay Initial delay before running the runnable.
     * @param period Subsequent delay before running the runnable.
     * @param timeUnit TimeUnit of the delay and period.
     * @return The scheduled task.
     */
    @NotNull
    public static ScheduledFuture<?> schedule(@NotNull Runnable runnable, long delay, long period, @NotNull TimeUnit timeUnit) {
        return EXECUTOR.scheduleAtFixedRate(runnable, delay, period, timeUnit);
    }
}