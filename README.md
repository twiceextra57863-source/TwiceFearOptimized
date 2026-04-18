# LagFree Vulkan Path (Fabric 1.21.4)

Bhai, iss repo ka goal ab clear hai: **Fabric 1.21.4 ke liye pure Vulkan renderer path research + implementation base**.

## Reality check (important)
- “100% lag-free for everyone” guarantee possible nahi hoti.
- Lekin **frametime stability + high FPS** target kiya ja sakta hai with engine-level work.

## Is commit me kya improve hua

1. Research document add: `docs/PURE_VULKAN_RESEARCH.md`
   - launcher/mobile lag causes
   - recording/discord/replay overhead breakdown
   - pure Vulkan architecture layers
   - milestone delivery plan

2. Multi-thread foundations add:
   - `CommandBufferScheduler`: command recording jobs ka worker scheduler
   - `AsyncChunkBuildQueue`: bounded async chunk build queue

3. Mixin hook expansion:
   - `RenderSystemMixin` add kiya to run Vulkan capability probe near renderer init lifecycle

4. Existing Vulkan scaffolding retained:
   - `VulkanDevice`, `MemoryManager`, `ShaderPipeline`

## Next immediate coding targets

- VkInstance/VkDevice/swapchain real initialization class
- frame graph + synchronization (timeline semaphore path)
- chunk mesh upload staging buffers
- shoreline foam + water tint low-cost pass
- dynamic render distance governor (frametime budget based)

## Reference links used in research

- Fabric 1.21.4 official post: https://fabricmc.net/2024/12/02/1214.html
- VulkanMod (real-world Vulkan renderer rewrite reference): https://github.com/xCollateral/VulkanMod
- Vulkan specification: https://registry.khronos.org/vulkan/specs/latest/html/vkspec.html
