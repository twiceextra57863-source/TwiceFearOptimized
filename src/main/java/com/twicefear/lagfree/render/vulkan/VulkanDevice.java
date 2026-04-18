package com.twicefear.lagfree.render.vulkan;

import com.twicefear.lagfree.LagFreeMod;
import org.lwjgl.system.Configuration;
import org.lwjgl.vulkan.VK;

public final class VulkanDevice {
    private VulkanDevice() {
    }

    public static void logRuntimeCapabilities() {
        try {
            final int version = VK.getInstanceVersionSupported();
            final int major = VK_VERSION_MAJOR(version);
            final int minor = VK_VERSION_MINOR(version);
            final int patch = VK_VERSION_PATCH(version);
            LagFreeMod.LOGGER.info("LWJGL Vulkan runtime detected: {}.{}.{}", major, minor, patch);
            if (major < 1 || (major == 1 && minor < 2)) {
                LagFreeMod.LOGGER.warn("Vulkan 1.2+ recommended; current runtime may not support planned renderer features.");
            }
        } catch (Throwable error) {
            LagFreeMod.LOGGER.warn("No Vulkan runtime detected. Falling back to OpenGL path.", error);
        }

        LagFreeMod.LOGGER.info("LWJGL debug allocator enabled: {}", Configuration.DEBUG_MEMORY_ALLOCATOR.get());
    }

    private static int VK_VERSION_MAJOR(int version) {
        return version >>> 22;
    }

    private static int VK_VERSION_MINOR(int version) {
        return (version >>> 12) & 0x3ff;
    }

    private static int VK_VERSION_PATCH(int version) {
        return version & 0xfff;
    }
}
