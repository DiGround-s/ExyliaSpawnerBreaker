package net.exylia.exyliaSpawnerBreaker.util;

import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ItemUtil {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ItemStack createItem(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (displayName != null && !displayName.isEmpty()) {
                meta.displayName(miniMessage.deserialize("<!italic>" + displayName));
            }

            if (lore != null && !lore.isEmpty()) {
                List<Component> componentLore = lore.stream()
                        .map(line -> miniMessage.deserialize("<!italic>" + line))
                        .collect(Collectors.toList());
                meta.lore(componentLore);
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (lore != null && !lore.isEmpty()) {
            List<Component> componentLore = lore.stream()
                    .map(line -> miniMessage.deserialize("<!italic>" + line))
                    .collect(Collectors.toList());
            meta.lore(componentLore);
        } else {
            meta.lore(null);
        }

        item.setItemMeta(meta);
    }

    public void setCustomModelData(ItemStack item, int customModelData) {
        if (customModelData <= 0) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
    }

    public void addEnchantment(ItemStack item, Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.addEnchant(enchantment, level, true);
        item.setItemMeta(meta);
    }

    public void hideEnchantments(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

    public boolean hasSpace(org.bukkit.entity.Player player) {
        return player.getInventory().firstEmpty() != -1;
    }
}
