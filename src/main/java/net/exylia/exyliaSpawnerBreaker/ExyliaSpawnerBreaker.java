package net.exylia.exyliaSpawnerBreaker;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;
import net.exylia.exyliaSpawnerBreaker.api.ExyliaSpawnerBreakerAPI;
import net.exylia.exyliaSpawnerBreaker.command.SpawnerBreakerCommand;
import net.exylia.exyliaSpawnerBreaker.config.cache.PickaxeConfigCache;
import net.exylia.exyliaSpawnerBreaker.integration.IntegrationManager;
import net.exylia.exyliaSpawnerBreaker.listener.BlockBreakListener;
import net.exylia.exyliaSpawnerBreaker.listener.ItemDamageListener;
import net.exylia.exyliaSpawnerBreaker.module.ExyliaSpawnerBreakerModule;
import net.exylia.exyliaSpawnerBreaker.service.PickaxeService;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class ExyliaSpawnerBreaker extends JavaPlugin {

    private static ExyliaSpawnerBreaker instance;
    private static ExyliaSpawnerBreakerAPI api;

    private Injector injector;
    private SpawnerBreakerCommand commandHandler;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        injector = Guice.createInjector(new ExyliaSpawnerBreakerModule(this));

        IntegrationManager integrationManager = injector.getInstance(IntegrationManager.class);
        getLogger().info("Integraciones cargadas:");
        integrationManager.getSpawnerIntegrations().forEach(integration ->
                getLogger().info("  - " + integration.getName() +
                        " (Prioridad: " + integration.getPriority() +
                        ", Habilitado: " + integration.isEnabled() + ")")
        );

        registerListeners();
        registerCommands();

        api = injector.getInstance(PickaxeService.class);

        getLogger().info("ExyliaSpawnerBreaker habilitado correctamente!");
    }

    @Override
    public void onDisable() {
        if (commandHandler != null) {
            commandHandler.getLamp().unregisterAllCommands();
        }

        PickaxeConfigCache cache = injector.getInstance(PickaxeConfigCache.class);
        cache.invalidateAll();

        getLogger().info("ExyliaSpawnerBreaker deshabilitado.");
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(injector.getInstance(BlockBreakListener.class), this);
        pm.registerEvents(injector.getInstance(ItemDamageListener.class), this);
    }

    private void registerCommands() {
        commandHandler = new SpawnerBreakerCommand(this, injector);
    }

    public static ExyliaSpawnerBreaker getInstance() {
        return instance;
    }

    public static ExyliaSpawnerBreakerAPI getAPI() {
        return api;
    }
}
