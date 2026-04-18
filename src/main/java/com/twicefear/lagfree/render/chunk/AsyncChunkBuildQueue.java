package com.twicefear.lagfree.render.chunk;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Lightweight bounded queue for async chunk build jobs.
 *
 * Bounded capacity prevents unbounded memory growth during fast traversal.
 */
public final class AsyncChunkBuildQueue implements AutoCloseable {
    private final BlockingQueue<Runnable> queue;
    private final ExecutorService workers;

    public AsyncChunkBuildQueue(int workerCount, int capacity) {
        if (workerCount < 1) {
            throw new IllegalArgumentException("workerCount must be >= 1");
        }
        if (capacity < 16) {
            throw new IllegalArgumentException("capacity must be >= 16");
        }
        this.queue = new ArrayBlockingQueue<>(capacity);
        this.workers = Executors.newFixedThreadPool(workerCount);

        for (int i = 0; i < workerCount; i++) {
            workers.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    Runnable job = queue.take();
                    job.run();
                }
                return null;
            });
        }
    }

    public boolean submit(Runnable job) {
        return queue.offer(job);
    }

    public int pendingJobs() {
        return queue.size();
    }

    @Override
    public void close() {
        workers.shutdownNow();
        queue.clear();
    }
}
