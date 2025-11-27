package net.exylia.exyliaSpawnerBreaker.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import net.exylia.exyliaSpawnerBreaker.config.holder.PickaxeConfigData;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Singleton
@Getter
public class PickaxeConfig {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private final Map<String, PickaxeConfigData> pickaxeTypes = new HashMap<>();
    private final Map<String, String> messages = new HashMap<>();

    private boolean particlesEnabled;
    private String particleType;
    private int particleAmount;
    private double particleOffsetX;
    private double particleOffsetY;
    private double particleOffsetZ;

    private boolean soundEnabled;
    private String soundType;
    private float soundVolume;
    private float soundPitch;

    private boolean actionbarEnabled;

    @Inject
    public PickaxeConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        loadPickaxeTypes();
        loadMessages();
        loadEffects();
    }

    public void reload() {
        pickaxeTypes.clear();
        messages.clear();
        load();
    }

    private void loadPickaxeTypes() {
        ConfigurationSection typesSection = config.getConfigurationSection("pickaxe-types");
        if (typesSection == null) return;

        for (String type : typesSection.getKeys(false)) {
            ConfigurationSection typeSection = typesSection.getConfigurationSection(type);
            if (typeSection == null) continue;

            boolean enabled = typeSection.getBoolean("enabled", true);
            if (!enabled) continue;

            Material material = Material.matchMaterial(typeSection.getString("material", "DIAMOND_PICKAXE"));
            if (material == null) material = Material.DIAMOND_PICKAXE;

            PickaxeConfigData data = PickaxeConfigData.builder()
                    .type(type)
                    .enabled(enabled)
                    .material(material)
                    .displayName(typeSection.getString("display-name", type))
                    .lore(typeSection.getStringList("lore"))
                    .defaultUses(typeSection.getInt("default-uses", 10))
                    .bypassProtection(typeSection.getBoolean("bypass-protection", false))
                    .customModelData(typeSection.getInt("custom-model-data", 0))
                    .enchantments(typeSection.getStringList("enchantments"))
                    .build();

            pickaxeTypes.put(type, data);
        }
    }

    private void loadMessages() {
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        if (messagesSection == null) return;

        for (String key : messagesSection.getKeys(false)) {
            messages.put(key, messagesSection.getString(key, ""));
        }
    }

    private void loadEffects() {
        ConfigurationSection effectsSection = config.getConfigurationSection("effects.break-spawner");
        if (effectsSection == null) return;

        ConfigurationSection particlesSection = effectsSection.getConfigurationSection("particles");
        if (particlesSection != null) {
            particlesEnabled = particlesSection.getBoolean("enabled", true);
            particleType = particlesSection.getString("type", "END_ROD");
            particleAmount = particlesSection.getInt("amount", 30);
            particleOffsetX = particlesSection.getDouble("offset-x", 0.5);
            particleOffsetY = particlesSection.getDouble("offset-y", 0.5);
            particleOffsetZ = particlesSection.getDouble("offset-z", 0.5);
        }

        ConfigurationSection soundSection = effectsSection.getConfigurationSection("sound");
        if (soundSection != null) {
            soundEnabled = soundSection.getBoolean("enabled", true);
            soundType = soundSection.getString("type", "BLOCK_GLASS_BREAK");
            soundVolume = (float) soundSection.getDouble("volume", 1.0);
            soundPitch = (float) soundSection.getDouble("pitch", 0.8);
        }

        ConfigurationSection actionbarSection = effectsSection.getConfigurationSection("actionbar");
        if (actionbarSection != null) {
            actionbarEnabled = actionbarSection.getBoolean("enabled", true);
        }
    }

    public Optional<PickaxeConfigData> getPickaxeType(String type) {
        return Optional.ofNullable(pickaxeTypes.get(type));
    }

    public Set<String> getPickaxeTypeNames() {
        return new HashSet<>(pickaxeTypes.keySet());
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, "");
    }

    public boolean isVirtualSpawnersEnabled() {
        return config.getBoolean("integrations.virtual-spawners.enabled", true);
    }

    public boolean isWorldGuardEnabled() {
        return config.getBoolean("integrations.worldguard.enabled", true);
    }
}
