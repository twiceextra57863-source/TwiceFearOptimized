package com.twicefear.lagfree.render.vulkan;

import java.util.concurrent.atomic.AtomicLong;

public final class MemoryManager {
    private final AtomicLong allocatedBytes = new AtomicLong();

    public long reserve(long bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException("bytes must be >= 0");
        }
        return allocatedBytes.addAndGet(bytes);
    }

    public long free(long bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException("bytes must be >= 0");
        }
        return allocatedBytes.updateAndGet(current -> Math.max(0, current - bytes));
    }

    public long allocatedBytes() {
        return allocatedBytes.get();
    }
}
