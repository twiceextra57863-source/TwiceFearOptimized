package com.twicefear.lagfree.mixin;

import com.twicefear.lagfree.LagFreeMod;
import com.twicefear.lagfree.perf.PerformanceTuner;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "reload", at = @At("HEAD"))
    private void lagfree$onReload(CallbackInfo ci) {
        PerformanceTuner.applySafeDefaults();
        LagFreeMod.LOGGER.debug("WorldRenderer reload hook called");
    }
}
