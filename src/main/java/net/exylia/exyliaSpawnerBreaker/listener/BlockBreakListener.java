package net.exylia.exyliaSpawnerBreaker.listener;

import com.google.inject.Inject;
import net.exylia.exyliaSpawnerBreaker.api.event.SpawnerBreakEvent;
import net.exylia.exyliaSpawnerBreaker.api.event.SpawnerBrokenEvent;
import net.exylia.exyliaSpawnerBreaker.config.cache.PickaxeConfigCache;
import net.exylia.exyliaSpawnerBreaker.config.holder.PickaxeConfigData;
import net.exylia.exyliaSpawnerBreaker.integration.IntegrationManager;
import net.exylia.exyliaSpawnerBreaker.model.SpawnerBreakResult;
import net.exylia.exyliaSpawnerBreaker.service.EffectService;
import net.exylia.exyliaSpawnerBreaker.service.PickaxeService;
import net.exylia.exyliaSpawnerBreaker.service.SpawnerBreakService;
import net.exylia.exyliaSpawnerBreaker.service.UsageService;
import net.exylia.exyliaSpawnerBreaker.util.MessageUtil;
import net.exylia.exyliaSpawnerBreaker.util.PersistentDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class BlockBreakListener implements Listener {

    private final PickaxeService pickaxeService;
    private final PersistentDataUtil pdcUtil;
    private final PickaxeConfigCache configCache;
    private final IntegrationManager integrationManager;
    private final SpawnerBreakService spawnerBreakService;
    private final UsageService usageService;
    private final EffectService effectService;
    private final MessageUtil messageUtil;

    @Inject
    public BlockBreakListener(
            PickaxeService pickaxeService,
            PersistentDataUtil pdcUtil,
            PickaxeConfigCache configCache,
            IntegrationManager integrationManager,
            SpawnerBreakService spawnerBreakService,
            UsageService usageService,
            EffectService effectService,
            MessageUtil messageUtil
    ) {
        this.pickaxeService = pickaxeService;
        this.pdcUtil = pdcUtil;
        this.configCache = configCache;
        this.integrationManager = integrationManager;
        this.spawnerBreakService = spawnerBreakService;
        this.usageService = usageService;
        this.effectService = effectService;
        this.messageUtil = messageUtil;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType() != Material.SPAWNER) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        Bukkit.getLogger().info("[DEBUG-BBL] Spawner break detected. Event cancelled: " + event.isCancelled());

        if (!pickaxeService.isPickaxe(item)) {
            Bukkit.getLogger().info("[DEBUG-BBL] Item is not a special pickaxe");
            return;
        }

        Bukkit.getLogger().info("[DEBUG-BBL] Item is special pickaxe, cancelling event completely");
        event.setCancelled(true);
        event.setDropItems(false);
        event.setExpToDrop(0);

        String pickaxeType = pdcUtil.getPickaxeType(item);
        if (pickaxeType == null) {
            Bukkit.getLogger().warning("[DEBUG-BBL] Pickaxe type is null");
            return;
        }

        Bukkit.getLogger().info("[DEBUG-BBL] Pickaxe type: " + pickaxeType);

        Optional<PickaxeConfigData> configOpt = configCache.get(pickaxeType);
        if (!configOpt.isPresent()) {
            Bukkit.getLogger().warning("[DEBUG-BBL] Config not found for type: " + pickaxeType);
            return;
        }

        PickaxeConfigData config = configOpt.get();
        Bukkit.getLogger().info("[DEBUG-BBL] Config loaded. Bypass protection: " + config.isBypassProtection());
        Bukkit.getLogger().info("[DEBUG-BBL] Pickaxe type: " + pickaxeType + ", bypass: " + config.isBypassProtection());

        if (!config.isBypassProtection()) {
            Bukkit.getLogger().info("[DEBUG-BBL] This pickaxe does NOT bypass protections, checking...");
            if (!integrationManager.canBreakProtectedBlock(player, block.getLocation())) {
                Bukkit.getLogger().warning("[DEBUG-BBL] Protected by WorldGuard!");
                messageUtil.send(player, "protected-spawner");
                return;
            }
            Bukkit.getLogger().info("[DEBUG-BBL] No protection or allowed to break");
        } else {
            Bukkit.getLogger().info("[DEBUG-BBL] This pickaxe BYPASSES protections! Skipping protection checks");
        }

        int currentUses = pdcUtil.getPickaxeUses(item);

        SpawnerBreakEvent breakEvent = new SpawnerBreakEvent(player, block, pickaxeType, currentUses);
        Bukkit.getPluginManager().callEvent(breakEvent);

        if (breakEvent.isCancelled()) {
            return;
        }

        Bukkit.getLogger().info("[DEBUG-BBL] About to call spawnerBreakService.handleBreak()");
        SpawnerBreakResult result = spawnerBreakService.handleBreak(player, block, item);

        if (!result.isSuccess()) {
            Bukkit.getLogger().warning("[DEBUG-BBL] handleBreak failed: " + result.getFailureReason());
            if ("INVENTORY_FULL".equals(result.getFailureReason())) {
                messageUtil.send(player, "inventory-full");
            }
            return;
        }

        Bukkit.getLogger().info("[DEBUG-BBL] handleBreak success! Now decreasing uses...");
        int usesBefore = pdcUtil.getPickaxeUses(item);
        Bukkit.getLogger().info("[DEBUG-BBL] Uses before: " + usesBefore);

        boolean pickaxeBroken = usageService.decreaseUses(player, item);
        int newUses = pickaxeBroken ? 0 : pdcUtil.getPickaxeUses(item);

        Bukkit.getLogger().info("[DEBUG-BBL] Uses after: " + newUses + ", pickaxe broken: " + pickaxeBroken);

        if (pickaxeBroken) {
            Bukkit.getLogger().info("[DEBUG-BBL] Pickaxe broken, skipping effects");
            SpawnerBrokenEvent brokenEvent = new SpawnerBrokenEvent(
                    player,
                    block.getLocation(),
                    pickaxeType,
                    0,
                    true
            );
            Bukkit.getPluginManager().callEvent(brokenEvent);
            return;
        }

        effectService.playBreakEffects(player, block.getLocation(), newUses);

        SpawnerBrokenEvent brokenEvent = new SpawnerBrokenEvent(
                player,
                block.getLocation(),
                pickaxeType,
                newUses,
                pickaxeBroken
        );
        Bukkit.getPluginManager().callEvent(brokenEvent);
    }
}
