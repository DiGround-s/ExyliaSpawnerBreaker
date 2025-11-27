package net.exylia.exyliaSpawnerBreaker.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SpawnerBreakResult {
    private final boolean success;
    private final String failureReason;
    private final int stackAmount;

    public static SpawnerBreakResult success(int stackAmount) {
        return new SpawnerBreakResult(true, null, stackAmount);
    }

    public static SpawnerBreakResult failed(String reason) {
        return new SpawnerBreakResult(false, reason, 0);
    }
}
