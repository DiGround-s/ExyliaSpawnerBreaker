package net.exylia.exyliaSpawnerBreaker.integration.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ProtectionIntegration {

    @NotNull
    String getName();

    boolean initialize();

    boolean canBreak(@NotNull Player player, @NotNull Location location);

    boolean isEnabled();
}
