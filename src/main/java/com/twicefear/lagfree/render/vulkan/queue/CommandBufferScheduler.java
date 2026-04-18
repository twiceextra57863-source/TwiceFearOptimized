package com.twicefear.lagfree.render.vulkan.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * JVM-side scheduler for multi-thread command recording jobs.
 *
 * This class does not directly create VkCommandBuffer handles yet; it defines
 * deterministic worker scheduling and frame-phase barriers used before native integration.
 */
public final class CommandBufferScheduler implements AutoCloseable {
    private final ExecutorService workers;

    public CommandBufferScheduler(int workerCount) {
        if (workerCount < 1) {
            throw new IllegalArgumentException("workerCount must be >= 1");
        }
        this.workers = Executors.newFixedThreadPool(workerCount);
    }

    public <T> List<T> recordBatch(List<Callable<T>> jobs) {
        try {
            List<Future<T>> futures = workers.invokeAll(jobs);
            List<T> result = new ArrayList<>(futures.size());
            for (Future<T> future : futures) {
                result.add(future.get());
            }
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Recording interrupted", e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Recording job failed", e.getCause());
        }
    }

    @Override
    public void close() {
        workers.shutdownNow();
    }
}
