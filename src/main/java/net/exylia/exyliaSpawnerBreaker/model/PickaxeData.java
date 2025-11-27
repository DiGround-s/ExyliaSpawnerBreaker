package net.exylia.exyliaSpawnerBreaker.model;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class PickaxeData {
    private final UUID id;
    private final String type;
    private final Material material;
    private final String displayName;
    private final List<String> lore;
    private final int uses;
    private final boolean bypassProtection;
    private final int customModelData;
    private final List<String> enchantments;
}
