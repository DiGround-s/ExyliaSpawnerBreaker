package net.exylia.exyliaSpawnerBreaker.api.event;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class SpawnerBrokenEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Location spawnerLocation;
    private final String pickaxeType;
    private final int remainingUses;
    private final boolean pickaxeBroken;

    public SpawnerBrokenEvent(
            Player player,
            Location spawnerLocation,
            String pickaxeType,
            int remainingUses,
            boolean pickaxeBroken
    ) {
        this.player = player;
        this.spawnerLocation = spawnerLocation;
        this.pickaxeType = pickaxeType;
        this.remainingUses = remainingUses;
        this.pickaxeBroken = pickaxeBroken;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
