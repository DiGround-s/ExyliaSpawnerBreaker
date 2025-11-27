package net.exylia.exyliaSpawnerBreaker.api;

import net.exylia.exyliaSpawnerBreaker.config.holder.PickaxeConfigData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ExyliaSpawnerBreakerAPI {

    @Nullable
    ItemStack createPickaxe(@NotNull String type, @Nullable Integer uses);

    boolean isPickaxe(@NotNull ItemStack item);

    int getRemainingUses(@NotNull ItemStack item);

    @Nullable
    String getPickaxeType(@NotNull ItemStack item);

    void setUses(@NotNull ItemStack item, int uses);

    @NotNull
    Set<String> getAvailableTypes();

    boolean isValidType(@NotNull String type);

    @Nullable
    PickaxeConfigData getPickaxeConfig(@NotNull String type);
}
