package net.exylia.exyliaSpawnerBreaker.config.holder;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@Getter
@Builder
public class PickaxeConfigData {
    private final String type;
    private final boolean enabled;
    private final Material material;
    private final String displayName;
    private final List<String> lore;
    private final int defaultUses;
    private final boolean bypassProtection;
    private final int customModelData;
    private final List<String> enchantments;
}
