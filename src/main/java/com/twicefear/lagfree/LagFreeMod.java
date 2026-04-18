package com.twicefear.lagfree;

import com.twicefear.lagfree.perf.PerformanceTuner;
import com.twicefear.lagfree.render.chunk.AsyncChunkBuildQueue;
import com.twicefear.lagfree.render.vulkan.VulkanDevice;
import com.twicefear.lagfree.render.vulkan.queue.CommandBufferScheduler;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LagFreeMod implements ClientModInitializer {
    public static final String MOD_ID = "lagfree";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static CommandBufferScheduler commandBufferScheduler;
    private static AsyncChunkBuildQueue chunkBuildQueue;

    @Override
    public void onInitializeClient() {
        LOGGER.info("LagFree bootstrap start");
        VulkanDevice.logRuntimeCapabilities();

        commandBufferScheduler = new CommandBufferScheduler(Math.max(1, Runtime.getRuntime().availableProcessors() / 2));
        chunkBuildQueue = new AsyncChunkBuildQueue(Math.max(1, Runtime.getRuntime().availableProcessors() / 3), 2048);
        PerformanceTuner.applySafeDefaults();

        LOGGER.info("LagFree schedulers initialized: commandWorkers={}, chunkQueuePending={}",
                Runtime.getRuntime().availableProcessors() / 2,
                chunkBuildQueue.pendingJobs());
    }

    public static CommandBufferScheduler commandBufferScheduler() {
        return commandBufferScheduler;
    }

    public static AsyncChunkBuildQueue chunkBuildQueue() {
        return chunkBuildQueue;
    }
}
