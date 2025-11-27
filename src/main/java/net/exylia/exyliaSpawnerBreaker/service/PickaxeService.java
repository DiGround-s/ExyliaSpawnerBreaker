package net.exylia.exyliaSpawnerBreaker.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.exylia.exyliaSpawnerBreaker.api.ExyliaSpawnerBreakerAPI;
import net.exylia.exyliaSpawnerBreaker.config.cache.PickaxeConfigCache;
import net.exylia.exyliaSpawnerBreaker.config.holder.PickaxeConfigData;
import net.exylia.exyliaSpawnerBreaker.factory.PickaxeFactory;
import net.exylia.exyliaSpawnerBreaker.util.PersistentDataUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

@Singleton
public class PickaxeService implements ExyliaSpawnerBreakerAPI {

    private final PickaxeFactory factory;
    private final PickaxeConfigCache cache;
    private final PersistentDataUtil pdcUtil;
    private final UsageService usageService;
    private final net.exylia.exyliaSpawnerBreaker.config.PickaxeConfig config;

    @Inject
    public PickaxeService(
            PickaxeFactory factory,
            PickaxeConfigCache cache,
            PersistentDataUtil pdcUtil,
            UsageService usageService,
            net.exylia.exyliaSpawnerBreaker.config.PickaxeConfig config
    ) {
        this.factory = factory;
        this.cache = cache;
        this.pdcUtil = pdcUtil;
        this.usageService = usageService;
        this.config = config;
    }

    @Override
    public @Nullable ItemStack createPickaxe(@NotNull String type, @Nullable Integer uses) {
        Optional<PickaxeConfigData> configOpt = cache.get(type);

        if (!configOpt.isPresent()) {
            return null;
        }

        PickaxeConfigData config = configOpt.get();
        int finalUses = uses != null ? uses : config.getDefaultUses();

        return factory.createPickaxe(config, finalUses);
    }

    @Override
    public boolean isPickaxe(@NotNull ItemStack item) {
        return pdcUtil.isPickaxe(item);
    }

    @Override
    public int getRemainingUses(@NotNull ItemStack item) {
        return pdcUtil.getPickaxeUses(item);
    }

    @Override
    public @Nullable String getPickaxeType(@NotNull ItemStack item) {
        return pdcUtil.getPickaxeType(item);
    }

    @Override
    public void setUses(@NotNull ItemStack item, int uses) {
        usageService.setUses(item, uses);
    }

    @Override
    public @NotNull Set<String> getAvailableTypes() {
        return config.getPickaxeTypeNames();
    }

    @Override
    public boolean isValidType(@NotNull String type) {
        Optional<PickaxeConfigData> config = cache.get(type);
        return config.isPresent();
    }

    @Override
    public @Nullable PickaxeConfigData getPickaxeConfig(@NotNull String type) {
        return cache.get(type).orElse(null);
    }
}
