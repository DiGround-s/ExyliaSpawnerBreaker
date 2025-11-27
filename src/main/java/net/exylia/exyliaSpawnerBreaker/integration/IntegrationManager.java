package net.exylia.exyliaSpawnerBreaker.integration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import net.exylia.exyliaSpawnerBreaker.integration.protection.ProtectionIntegration;
import net.exylia.exyliaSpawnerBreaker.integration.protection.WorldGuardIntegration;
import net.exylia.exyliaSpawnerBreaker.integration.spawner.SpawnerIntegration;
import net.exylia.exyliaSpawnerBreaker.integration.spawner.VanillaSpawnerIntegration;
import net.exylia.exyliaSpawnerBreaker.integration.spawner.VirtualSpawnersIntegration;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Singleton
@Getter
public class IntegrationManager {

    private final List<SpawnerIntegration> spawnerIntegrations = new ArrayList<>();
    private final List<ProtectionIntegration> protectionIntegrations = new ArrayList<>();
    private final JavaPlugin plugin;

    @Inject
    public IntegrationManager(
            VirtualSpawnersIntegration virtualSpawners,
            VanillaSpawnerIntegration vanilla,
            WorldGuardIntegration worldGuard,
            JavaPlugin plugin
    ) {
        this.plugin = plugin;

        registerSpawnerIntegration(virtualSpawners);
        registerSpawnerIntegration(vanilla);

        registerProtectionIntegration(worldGuard);
    }

    public void registerSpawnerIntegration(SpawnerIntegration integration) {
        if (integration.initialize()) {
            spawnerIntegrations.add(integration);
            spawnerIntegrations.sort(
                    Comparator.comparingInt(SpawnerIntegration::getPriority).reversed()
            );
            plugin.getLogger().info("Registered spawner integration: " + integration.getName() +
                       " (Priority: " + integration.getPriority() + ")");
        } else {
            plugin.getLogger().info("Spawner integration not available: " + integration.getName());
        }
    }

    public void registerProtectionIntegration(ProtectionIntegration integration) {
        if (integration.initialize()) {
            protectionIntegrations.add(integration);
            plugin.getLogger().info("Registered protection integration: " + integration.getName());
        } else {
            plugin.getLogger().info("Protection integration not available: " + integration.getName());
        }
    }

    @Nullable
    public SpawnerIntegration getApplicableIntegration(Block block) {
        plugin.getLogger().info("[DEBUG-IM] Searching for applicable integration for block at " +
                block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ());
        plugin.getLogger().info("[DEBUG-IM] Total registered integrations: " + spawnerIntegrations.size());

        for (SpawnerIntegration integration : spawnerIntegrations) {
            plugin.getLogger().info("[DEBUG-IM] Checking " + integration.getName() +
                    " - Enabled: " + integration.isEnabled() +
                    ", Priority: " + integration.getPriority());

            if (!integration.isEnabled()) {
                plugin.getLogger().info("[DEBUG-IM] " + integration.getName() + " is disabled, skipping");
                continue;
            }

            boolean isSpawner = integration.isSpawner(block);
            plugin.getLogger().info("[DEBUG-IM] " + integration.getName() + ".isSpawner() = " + isSpawner);

            if (isSpawner) {
                plugin.getLogger().info("[DEBUG-IM] Selected integration: " + integration.getName());
                return integration;
            }
        }

        plugin.getLogger().warning("[DEBUG-IM] No integration found for block!");
        return null;
    }

    public boolean canBreakProtectedBlock(Player player, Location location) {
        plugin.getLogger().info("[DEBUG-IM] Checking protections for " + player.getName() +
                " at " + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ());
        plugin.getLogger().info("[DEBUG-IM] Total protection integrations: " + protectionIntegrations.size());

        for (ProtectionIntegration protection : protectionIntegrations) {
            plugin.getLogger().info("[DEBUG-IM] Checking " + protection.getName() +
                    " - Enabled: " + protection.isEnabled());

            if (!protection.isEnabled()) {
                plugin.getLogger().info("[DEBUG-IM] " + protection.getName() + " is disabled, skipping");
                continue;
            }

            boolean canBreak = protection.canBreak(player, location);
            plugin.getLogger().info("[DEBUG-IM] " + protection.getName() + ".canBreak() = " + canBreak);

            if (!canBreak) {
                plugin.getLogger().warning("[DEBUG-IM] Protection " + protection.getName() + " denied break!");
                return false;
            }
        }

        plugin.getLogger().info("[DEBUG-IM] All protections passed!");
        return true;
    }
}
