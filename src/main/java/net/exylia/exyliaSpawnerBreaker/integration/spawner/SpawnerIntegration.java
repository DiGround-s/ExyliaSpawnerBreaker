package net.exylia.exyliaSpawnerBreaker.integration.spawner;

import net.exylia.exyliaSpawnerBreaker.model.SpawnerBreakResult;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SpawnerIntegration {

    @NotNull
    String getName();

    boolean initialize();

    boolean isSpawner(@NotNull Block block);

    @NotNull
    SpawnerBreakResult handleBreak(@NotNull Player player, @NotNull Block spawner);

    int getStackAmount(@NotNull Block spawner);

    int getPriority();

    boolean isEnabled();
}
