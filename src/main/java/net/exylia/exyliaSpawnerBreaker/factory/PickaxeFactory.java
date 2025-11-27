package net.exylia.exyliaSpawnerBreaker.factory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.exylia.exyliaSpawnerBreaker.config.holder.PickaxeConfigData;
import net.exylia.exyliaSpawnerBreaker.util.ItemUtil;
import net.exylia.exyliaSpawnerBreaker.util.MessageUtil;
import net.exylia.exyliaSpawnerBreaker.util.PersistentDataUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PickaxeFactory {

    private final ItemUtil itemUtil;
    private final PersistentDataUtil pdcUtil;
    private final MessageUtil messageUtil;

    @Inject
    public PickaxeFactory(ItemUtil itemUtil, PersistentDataUtil pdcUtil, MessageUtil messageUtil) {
        this.itemUtil = itemUtil;
        this.pdcUtil = pdcUtil;
        this.messageUtil = messageUtil;
    }

    public ItemStack createPickaxe(PickaxeConfigData config, int uses) {
        List<String> processedLore = config.getLore().stream()
                .map(line -> messageUtil.replace(line, "{uses}", String.valueOf(uses)))
                .collect(Collectors.toList());

        ItemStack pickaxe = itemUtil.createItem(
                config.getMaterial(),
                config.getDisplayName(),
                processedLore
        );

        if (config.getCustomModelData() > 0) {
            itemUtil.setCustomModelData(pickaxe, config.getCustomModelData());
        }

        for (String enchantString : config.getEnchantments()) {
            parseAndApplyEnchantment(pickaxe, enchantString);
        }

        pdcUtil.setPickaxeData(pickaxe, config.getType(), uses);

        return pickaxe;
    }

    private void parseAndApplyEnchantment(ItemStack item, String enchantString) {
        String[] parts = enchantString.split(":");
        if (parts.length != 2) {
            return;
        }

        try {
            Enchantment enchantment = Enchantment.getByName(parts[0].toUpperCase());
            if (enchantment == null) {
                return;
            }

            int level = Integer.parseInt(parts[1]);
            itemUtil.addEnchantment(item, enchantment, level);
        } catch (NumberFormatException e) {
        }
    }
}
