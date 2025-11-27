package net.exylia.exyliaSpawnerBreaker.listener;

import com.google.inject.Inject;
import net.exylia.exyliaSpawnerBreaker.util.PersistentDataUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDamageListener implements Listener {

    private final PersistentDataUtil pdcUtil;

    @Inject
    public ItemDamageListener(PersistentDataUtil pdcUtil) {
        this.pdcUtil = pdcUtil;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        if (pdcUtil.isPickaxe(item)) {
            event.setCancelled(true);
        }
    }
}
