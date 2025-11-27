package net.exylia.exyliaSpawnerBreaker.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

@Singleton
public class PersistentDataUtil {

    private final NamespacedKey PICKAXE_TYPE;
    private final NamespacedKey PICKAXE_USES;
    private final NamespacedKey PICKAXE_ID;

    @Inject
    public PersistentDataUtil(JavaPlugin plugin) {
        this.PICKAXE_TYPE = new NamespacedKey(plugin, "pickaxe_type");
        this.PICKAXE_USES = new NamespacedKey(plugin, "pickaxe_uses");
        this.PICKAXE_ID = new NamespacedKey(plugin, "pickaxe_id");
    }

    public boolean isPickaxe(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        return pdc.has(PICKAXE_TYPE, PersistentDataType.STRING);
    }

    public void setPickaxeData(ItemStack item, String type, int uses) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(PICKAXE_TYPE, PersistentDataType.STRING, type);
        pdc.set(PICKAXE_USES, PersistentDataType.INTEGER, uses);
        pdc.set(PICKAXE_ID, PersistentDataType.STRING, UUID.randomUUID().toString());

        item.setItemMeta(meta);
    }

    public String getPickaxeType(ItemStack item) {
        if (!isPickaxe(item)) return null;

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        return pdc.get(PICKAXE_TYPE, PersistentDataType.STRING);
    }

    public int getPickaxeUses(ItemStack item) {
        if (!isPickaxe(item)) return -1;

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        Integer uses = pdc.get(PICKAXE_USES, PersistentDataType.INTEGER);
        return uses != null ? uses : -1;
    }

    public void setPickaxeUses(ItemStack item, int uses) {
        if (!isPickaxe(item)) return;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(PICKAXE_USES, PersistentDataType.INTEGER, uses);
        item.setItemMeta(meta);
    }

    public String getPickaxeId(ItemStack item) {
        if (!isPickaxe(item)) return null;

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        return pdc.get(PICKAXE_ID, PersistentDataType.STRING);
    }
}
