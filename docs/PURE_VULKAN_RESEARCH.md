# Pure Vulkan Renderer for Fabric 1.21.4 — Practical Research

_Date: 2026-04-18_

## 1) Straight answer first

**Haan, technically possible hai** — lekin ye normal optimization mod nahi hai.
Ye Minecraft Java ke render stack ka **engine-level rewrite** hai.

Agar target "pure Vulkan renderer" hai, toh approach mostly in points jaisa hoga:
- OpenGL/RenderSystem path bypass + replacement
- Fabric Mixins ke through render pipeline interception
- Vulkan core (instance/device/swapchain/queues/sync)
- Multi-threaded command recording
- Async chunk mesh build + upload
- SPIR-V shader toolchain
- Compatibility fallback + crash-safe mode

## 2) Confirmed ecosystem facts (important)

1. **Fabric 1.21.4 blog (Dec 2, 2024)** explicitly recommends Loom 1.9 + latest stable Loader at the time (0.16.9).  
2. Existing open-source project **VulkanMod** demonstrates that Minecraft Java renderer replacement with Vulkan is possible and calls itself a full renderer rewrite.  
3. VulkanMod also shows real-world tradeoff: compatibility issues with many mods that assume OpenGL behavior.

## 3) Why FPS drops happen in your use-cases

### A) Recording / Discord / Screen Share
- GPU video encoder + game renderer contention
- CPU thread contention (game loop + encoder + audio + network)
- upload/download spikes due texture/frame copies

### B) PVP hit/crit stutter
- client frame pacing instability (not only raw FPS)
- packet burst timing + interpolation gaps
- particle/post effect spikes on hit/flame

### C) Big SMP entity scenes
- visibility/culling cost + draw submission overhead
- block entity and entity animation updates
- chunk rebuild storms when many changes occur

### D) Render distance regression (especially on phone launchers)
- thermal throttling and driver limits
- chunk meshing + upload bandwidth ceiling
- background process pressure (Android/desktop)

## 4) Real architecture needed for "pure Vulkan"

## Layer 0: Bootstrap
- Vulkan runtime probe (`vkEnumerateInstanceVersion` equivalent)
- feature matrix detect (descriptor indexing, timeline semaphore, indirect draw)
- fallback mode if unsupported

## Layer 1: Core Vulkan
- `VkInstance`, `VkPhysicalDevice`, `VkDevice`
- graphics/present/transfer queues
- swapchain + framebuffers + renderpass or dynamic rendering path
- synchronization plan (timeline semaphores preferred)

## Layer 2: Resource system
- device memory allocator (suballocation strategy)
- staging buffers + async upload queue
- texture atlas upload scheduler

## Layer 3: Shader pipeline
- GLSL -> SPIR-V compile (build-time recommended)
- shader variant cache + pipeline cache
- descriptor set layouts for terrain/entities/particles/ui

## Layer 4: World rendering
- async chunk build workers
- region/section visibility graph
- multi-draw indirect path where possible
- transparent sorting strategy (cost bounded)

## Layer 5: Gameplay smoothness systems
- frame-time budget governor (dynamic render distance + effect LOD)
- hit/flame camera shake normalization
- particle budget adaptation on low-end hardware

## 5) Expected compatibility reality

A pure Vulkan backend breaks assumptions of many OpenGL-centric mods.
So 2 modes rakhna better hai:
1. `strict_vulkan=true` (max performance, lower compatibility)
2. `compat_mode=true` (keeps selected GL-like behavior or feature downgrades)

## 6) Water visuals + performance target

For your requested cleaner blue water + shoreline foam/waves:
- cheap shoreline mask from depth/terrain edge info
- low-cost normal perturbation
- single-pass foam threshold near land edges
- configurable quality levels (LOW/MED/HIGH)

Important: this should be optional on low-end devices.

## 7) Delivery plan (milestones)

### Milestone A (2–4 weeks)
- bootstrap Vulkan device
- swapchain render clear test
- one simple world pass drawn

### Milestone B (4–8 weeks)
- chunk mesh upload path + async workers
- descriptor/pipeline cache
- entity render baseline

### Milestone C (8–12+ weeks)
- combat/frame pacing improvements
- water pass + foam
- dynamic render-distance governor
- recording profile presets

## 8) KPI targets (realistic)

- 1% low FPS + frametime variance improve over baseline
- chunk rebuild time per section reduced
- stutter spikes > 50ms reduced in SMP hubs
- stable performance under recording mode profile

## 9) Decision summary

Agar goal "bahut zyada FPS + stutter kam" hai, toh best path:
- Vulkan rewrite continue karo,
- but staged and measured way me,
- with telemetry and fallback modes,
- and avoid promising "zero lag".

Ye file implementation roadmap ke liye source-of-truth treat karo.
