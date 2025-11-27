package net.exylia.exyliaSpawnerBreaker.integration.spawner;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.codava.virtualspawner.api.VirtualSpawnerAPI;
import net.codava.virtualspawner.api.interfaces.IVirtualSpawner;
import net.exylia.exyliaSpawnerBreaker.config.PickaxeConfig;
import net.exylia.exyliaSpawnerBreaker.model.SpawnerBreakResult;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Singleton
public class VirtualSpawnersIntegration implements SpawnerIntegration {

    private final PickaxeConfig config;
    private boolean enabled = false;

    @Inject
    public VirtualSpawnersIntegration(PickaxeConfig config) {
        this.config = config;
    }

    @Override
    public @NotNull String getName() {
        return "VirtualSpawners";
    }

    @Override
    public boolean initialize() {
        Bukkit.getLogger().info("[DEBUG-VS] Initializing VirtualSpawners integration...");
        Bukkit.getLogger().info("[DEBUG-VS] Config enabled: " + config.isVirtualSpawnersEnabled());

        if (!config.isVirtualSpawnersEnabled()) {
            Bukkit.getLogger().info("[DEBUG-VS] VirtualSpawners disabled in config");
            return false;
        }

        try {
            Class.forName("net.codava.virtualspawner.api.VirtualSpawnerAPI");
            enabled = Bukkit.getPluginManager().isPluginEnabled("VirtualSpawner");
            Bukkit.getLogger().info("[DEBUG-VS] VirtualSpawner plugin found: " + enabled);
            return enabled;
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().warning("[DEBUG-VS] VirtualSpawner API not found: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isSpawner(@NotNull Block block) {
        Bukkit.getLogger().info("[DEBUG-VS] isSpawner called for block at " +
                block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ());
        Bukkit.getLogger().info("[DEBUG-VS] Enabled: " + enabled);
        Bukkit.getLogger().info("[DEBUG-VS] Block type: " + block.getType());

        if (!enabled || block.getType() != Material.SPAWNER) {
            Bukkit.getLogger().info("[DEBUG-VS] Not a spawner or integration disabled");
            return false;
        }

        Optional<IVirtualSpawner> spawnerOpt = VirtualSpawnerAPI.getSpawner(block.getLocation());
        boolean present = spawnerOpt.isPresent();
        Bukkit.getLogger().info("[DEBUG-VS] VirtualSpawner present: " + present);

        if (present) {
            IVirtualSpawner vs = spawnerOpt.get();
            Bukkit.getLogger().info("[DEBUG-VS] Stack amount: " + vs.getStack());
        }

        return present;
    }

    @Override
    public @NotNull SpawnerBreakResult handleBreak(@NotNull Player player, @NotNull Block spawner) {
        Bukkit.getLogger().info("[DEBUG-VS] handleBreak called");
        Optional<IVirtualSpawner> spawnerOpt = VirtualSpawnerAPI.getSpawner(spawner.getLocation());

        if (spawnerOpt.isEmpty()) {
            Bukkit.getLogger().warning("[DEBUG-VS] No VirtualSpawner found at location!");
            return SpawnerBreakResult.failed("No VirtualSpawner found at location");
        }

        IVirtualSpawner virtualSpawner = spawnerOpt.get();
        long stack = virtualSpawner.getStack();
        Bukkit.getLogger().info("[DEBUG-VS] Current stack: " + stack);

        ItemStack spawnerItem = virtualSpawner.getSpawnerConfigData().getItem(1);
        Bukkit.getLogger().info("[DEBUG-VS] Spawner item: " + spawnerItem.getType() + " x" + spawnerItem.getAmount());

        Bukkit.getLogger().info("[DEBUG-VS] Adding item to player inventory FIRST");
        player.getInventory().addItem(spawnerItem);

        if (stack > 1) {
            Bukkit.getLogger().info("[DEBUG-VS] Stack > 1, removing 1 from stack WITHOUT drops");
            virtualSpawner.setStack((int) (stack - 1));
            Bukkit.getLogger().info("[DEBUG-VS] New stack: " + virtualSpawner.getStack());
        } else {
            Bukkit.getLogger().info("[DEBUG-VS] Stack = 1, removing spawner completely using VirtualSpawner API");
            virtualSpawner.remove();
            Bukkit.getLogger().info("[DEBUG-VS] VirtualSpawner removed, now setting block to AIR");
            spawner.setType(Material.AIR);
        }

        return SpawnerBreakResult.success((int) stack);
    }

    @Override
    public int getStackAmount(@NotNull Block spawner) {
        if (!enabled) {
            return 1;
        }

        Optional<IVirtualSpawner> spawnerOpt = VirtualSpawnerAPI.getSpawner(spawner.getLocation());
        return spawnerOpt.map(s -> (int) s.getStack()).orElse(1);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
