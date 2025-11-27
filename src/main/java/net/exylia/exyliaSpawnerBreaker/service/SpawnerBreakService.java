package net.exylia.exyliaSpawnerBreaker.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.exylia.exyliaSpawnerBreaker.integration.IntegrationManager;
import net.exylia.exyliaSpawnerBreaker.integration.spawner.SpawnerIntegration;
import net.exylia.exyliaSpawnerBreaker.model.SpawnerBreakResult;
import net.exylia.exyliaSpawnerBreaker.util.ItemUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Singleton
public class SpawnerBreakService {

    private final IntegrationManager integrationManager;
    private final ItemUtil itemUtil;

    @Inject
    public SpawnerBreakService(IntegrationManager integrationManager, ItemUtil itemUtil) {
        this.integrationManager = integrationManager;
        this.itemUtil = itemUtil;
    }

    @NotNull
    public SpawnerBreakResult handleBreak(@NotNull Player player, @NotNull Block spawner, @NotNull ItemStack pickaxe) {
        org.bukkit.Bukkit.getLogger().info("[DEBUG-SBS] Checking inventory space...");
        if (!itemUtil.hasSpace(player)) {
            org.bukkit.Bukkit.getLogger().warning("[DEBUG-SBS] Inventory full!");
            return SpawnerBreakResult.failed("INVENTORY_FULL");
        }
        org.bukkit.Bukkit.getLogger().info("[DEBUG-SBS] Inventory has space");

        org.bukkit.Bukkit.getLogger().info("[DEBUG-SBS] Getting applicable integration...");
        SpawnerIntegration integration = integrationManager.getApplicableIntegration(spawner);

        if (integration == null) {
            org.bukkit.Bukkit.getLogger().warning("[DEBUG-SBS] No integration found!");
            return SpawnerBreakResult.failed("NO_INTEGRATION");
        }

        org.bukkit.Bukkit.getLogger().info("[DEBUG-SBS] Using integration: " + integration.getName());
        org.bukkit.Bukkit.getLogger().info("[DEBUG-SBS] Calling integration.handleBreak()...");
        SpawnerBreakResult result = integration.handleBreak(player, spawner);
        org.bukkit.Bukkit.getLogger().info("[DEBUG-SBS] Result: success=" + result.isSuccess() + ", reason=" + result.getFailureReason());

        return result;
    }
}
