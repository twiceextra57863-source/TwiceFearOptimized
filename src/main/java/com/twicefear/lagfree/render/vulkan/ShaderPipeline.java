package com.twicefear.lagfree.render.vulkan;

import java.nio.file.Path;

public final class ShaderPipeline {
    public record ShaderModule(Path spirvPath, String stage) {}

    public ShaderModule registerSpirv(Path spirvPath, String stage) {
        if (spirvPath == null || stage == null || stage.isBlank()) {
            throw new IllegalArgumentException("SPIR-V path and stage are required");
        }
        return new ShaderModule(spirvPath, stage);
    }
}
