package net.exylia.exyliaSpawnerBreaker.module;

import com.google.inject.AbstractModule;
import net.exylia.exyliaSpawnerBreaker.ExyliaSpawnerBreaker;
import org.bukkit.plugin.java.JavaPlugin;

public class ExyliaSpawnerBreakerModule extends AbstractModule {

    private final ExyliaSpawnerBreaker plugin;

    public ExyliaSpawnerBreakerModule(ExyliaSpawnerBreaker plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(ExyliaSpawnerBreaker.class).toInstance(plugin);
        bind(JavaPlugin.class).toInstance(plugin);
    }
}
