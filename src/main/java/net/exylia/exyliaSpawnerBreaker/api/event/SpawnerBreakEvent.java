package net.exylia.exyliaSpawnerBreaker.api.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class SpawnerBreakEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Block spawner;
    private final String pickaxeType;
    private final int remainingUses;
    private boolean cancelled = false;

    public SpawnerBreakEvent(Player player, Block spawner, String pickaxeType, int remainingUses) {
        this.player = player;
        this.spawner = spawner;
        this.pickaxeType = pickaxeType;
        this.remainingUses = remainingUses;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
