package net.exylia.exyliaSpawnerBreaker.config.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.exylia.exyliaSpawnerBreaker.config.PickaxeConfig;
import net.exylia.exyliaSpawnerBreaker.config.holder.PickaxeConfigData;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
public class PickaxeConfigCache {

    private final LoadingCache<String, Optional<PickaxeConfigData>> cache;
    private final PickaxeConfig config;

    @Inject
    public PickaxeConfigCache(PickaxeConfig config) {
        this.config = config;

        this.cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .refreshAfterWrite(1, TimeUnit.MINUTES)
                .recordStats()
                .build(new CacheLoader<String, Optional<PickaxeConfigData>>() {
                    @Override
                    public @Nullable Optional<PickaxeConfigData> load(String type) {
                        return config.getPickaxeType(type);
                    }
                });
    }

    public Optional<PickaxeConfigData> get(String type) {
        return cache.get(type);
    }

    public void invalidate(String type) {
        cache.invalidate(type);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public CacheStats getStats() {
        return cache.stats();
    }

    public long size() {
        return cache.estimatedSize();
    }
}
