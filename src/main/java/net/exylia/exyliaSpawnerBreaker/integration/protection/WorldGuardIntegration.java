package net.exylia.exyliaSpawnerBreaker.integration.protection;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.exylia.exyliaSpawnerBreaker.config.PickaxeConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Singleton
public class WorldGuardIntegration implements ProtectionIntegration {

    private final PickaxeConfig config;
    private boolean enabled = false;

    @Inject
    public WorldGuardIntegration(PickaxeConfig config) {
        this.config = config;
    }

    @Override
    public @NotNull String getName() {
        return "WorldGuard";
    }

    @Override
    public boolean initialize() {
        Bukkit.getLogger().info("[DEBUG-WG] Initializing WorldGuard integration...");
        Bukkit.getLogger().info("[DEBUG-WG] Config enabled: " + config.isWorldGuardEnabled());

        if (!config.isWorldGuardEnabled()) {
            Bukkit.getLogger().info("[DEBUG-WG] WorldGuard disabled in config");
            return false;
        }

        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
            enabled = Bukkit.getPluginManager().isPluginEnabled("WorldGuard");
            Bukkit.getLogger().info("[DEBUG-WG] WorldGuard plugin found: " + enabled);
            return enabled;
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().warning("[DEBUG-WG] WorldGuard API not found: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean canBreak(@NotNull Player player, @NotNull Location location) {
        Bukkit.getLogger().info("[DEBUG-WG] canBreak called for " + player.getName());
        Bukkit.getLogger().info("[DEBUG-WG] Enabled: " + enabled);

        if (!enabled) {
            Bukkit.getLogger().info("[DEBUG-WG] WorldGuard disabled, allowing break");
            return true;
        }

        try {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(location);

            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            boolean canBreak = query.testState(wgLocation, localPlayer, Flags.BLOCK_BREAK);

            Bukkit.getLogger().info("[DEBUG-WG] WorldGuard check result: " + canBreak);
            return canBreak;
        } catch (Exception e) {
            Bukkit.getLogger().warning("[DEBUG-WG] Error checking WorldGuard: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
