package net.exylia.exyliaSpawnerBreaker.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.exylia.exyliaSpawnerBreaker.config.cache.PickaxeConfigCache;
import net.exylia.exyliaSpawnerBreaker.config.holder.PickaxeConfigData;
import net.exylia.exyliaSpawnerBreaker.util.ItemUtil;
import net.exylia.exyliaSpawnerBreaker.util.MessageUtil;
import net.exylia.exyliaSpawnerBreaker.util.PersistentDataUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class UsageService {

    private final PersistentDataUtil pdcUtil;
    private final ItemUtil itemUtil;
    private final MessageUtil messageUtil;
    private final PickaxeConfigCache configCache;

    @Inject
    public UsageService(
            PersistentDataUtil pdcUtil,
            ItemUtil itemUtil,
            MessageUtil messageUtil,
            PickaxeConfigCache configCache
    ) {
        this.pdcUtil = pdcUtil;
        this.itemUtil = itemUtil;
        this.messageUtil = messageUtil;
        this.configCache = configCache;
    }

    public boolean decreaseUses(Player player, ItemStack pickaxe) {
        if (!pdcUtil.isPickaxe(pickaxe)) {
            return false;
        }

        int currentUses = pdcUtil.getPickaxeUses(pickaxe);
        if (currentUses <= 0) {
            return false;
        }

        int newUses = currentUses - 1;

        if (newUses <= 0) {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            messageUtil.send(player, "pickaxe-broken");
            return true;
        }

        pdcUtil.setPickaxeUses(pickaxe, newUses);
        updateLore(pickaxe, newUses);

        return false;
    }

    private void updateLore(ItemStack pickaxe, int newUses) {
        String pickaxeType = pdcUtil.getPickaxeType(pickaxe);
        if (pickaxeType == null) return;

        Optional<PickaxeConfigData> configOpt = configCache.get(pickaxeType);
        if (!configOpt.isPresent()) return;

        PickaxeConfigData config = configOpt.get();

        List<String> updatedLore = config.getLore().stream()
                .map(line -> messageUtil.replace(line, "{uses}", String.valueOf(newUses)))
                .collect(Collectors.toList());

        itemUtil.setLore(pickaxe, updatedLore);
    }

    public int getRemainingUses(ItemStack pickaxe) {
        return pdcUtil.getPickaxeUses(pickaxe);
    }

    public void setUses(ItemStack pickaxe, int uses) {
        if (!pdcUtil.isPickaxe(pickaxe)) {
            return;
        }

        pdcUtil.setPickaxeUses(pickaxe, uses);
        updateLore(pickaxe, uses);
    }
}
