package net.exylia.exyliaSpawnerBreaker.integration.spawner;

import com.google.inject.Singleton;
import net.exylia.exyliaSpawnerBreaker.model.SpawnerBreakResult;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

@Singleton
public class VanillaSpawnerIntegration implements SpawnerIntegration {

    private boolean enabled = false;

    @Override
    public @NotNull String getName() {
        return "Vanilla";
    }

    @Override
    public boolean initialize() {
        enabled = true;
        return true;
    }

    @Override
    public boolean isSpawner(@NotNull Block block) {
        boolean isSpawner = block.getType() == Material.SPAWNER;
        org.bukkit.Bukkit.getLogger().info("[DEBUG-VANILLA] isSpawner called - Result: " + isSpawner);
        return isSpawner;
    }

    @Override
    public @NotNull SpawnerBreakResult handleBreak(@NotNull Player player, @NotNull Block spawner) {
        org.bukkit.Bukkit.getLogger().info("[DEBUG-VANILLA] handleBreak called");

        if (spawner.getState() instanceof CreatureSpawner) {
            CreatureSpawner creatureSpawner = (CreatureSpawner) spawner.getState();
            org.bukkit.Bukkit.getLogger().info("[DEBUG-VANILLA] Spawner type: " + creatureSpawner.getSpawnedType());

            ItemStack spawnerItem = new ItemStack(Material.SPAWNER);
            BlockStateMeta meta = (BlockStateMeta) spawnerItem.getItemMeta();

            if (meta != null) {
                CreatureSpawner spawnerState = (CreatureSpawner) meta.getBlockState();
                spawnerState.setSpawnedType(creatureSpawner.getSpawnedType());
                meta.setBlockState(spawnerState);
                spawnerItem.setItemMeta(meta);
            }

            org.bukkit.Bukkit.getLogger().info("[DEBUG-VANILLA] Adding item to inventory and removing block");
            player.getInventory().addItem(spawnerItem);
            spawner.setType(Material.AIR);

            return SpawnerBreakResult.success(1);
        }

        org.bukkit.Bukkit.getLogger().warning("[DEBUG-VANILLA] Not a valid spawner!");
        return SpawnerBreakResult.failed("Not a valid spawner");
    }

    @Override
    public int getStackAmount(@NotNull Block spawner) {
        return 1;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
