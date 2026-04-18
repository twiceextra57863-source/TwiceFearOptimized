package com.twicefear.lagfree.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.twicefear.lagfree.LagFreeMod;
import com.twicefear.lagfree.render.vulkan.VulkanDevice;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(method = "initRenderer", at = @At("TAIL"), remap = false)
    private static void lagfree$afterInitRenderer(int debugVerbosity, boolean debugSync, CallbackInfo ci) {
        LagFreeMod.LOGGER.info("RenderSystem initialized. Running Vulkan probe and feature matrix check.");
        VulkanDevice.logRuntimeCapabilities();
    }
}
