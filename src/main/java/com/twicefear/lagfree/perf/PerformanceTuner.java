package com.twicefear.lagfree.perf;

import com.twicefear.lagfree.LagFreeMod;

public final class PerformanceTuner {
    private PerformanceTuner() {
    }

    public static void applySafeDefaults() {
        // Intentional no-op for now. Place dynamic option tuning here.
        LagFreeMod.LOGGER.debug("PerformanceTuner.applySafeDefaults() executed");
    }
}
