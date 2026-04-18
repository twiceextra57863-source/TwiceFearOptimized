package com.twicefear.lagfree.mixin;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    /**
     * Reduces camera shake strength when hurt/burning by scaling yaw influence.
     */
    @ModifyArg(method = "bobView", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;rotate(FFFF)Lorg/joml/Matrix4f;"), index = 0)
    private float lagfree$reduceHurtCamAmplitude(float angle) {
        return angle * 0.35f;
    }
}
