package net.exylia.exyliaSpawnerBreaker.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.exylia.exyliaSpawnerBreaker.config.PickaxeConfig;
import net.exylia.exyliaSpawnerBreaker.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Singleton
public class EffectService {

    private final PickaxeConfig config;
    private final MessageUtil messageUtil;

    @Inject
    public EffectService(PickaxeConfig config, MessageUtil messageUtil) {
        this.config = config;
        this.messageUtil = messageUtil;
    }

    public void playBreakEffects(Player player, Location spawnerLocation, int remainingUses) {
        playParticles(spawnerLocation);
        playSound(player, spawnerLocation);
        showActionBar(player, remainingUses);
    }

    private void playParticles(Location location) {
        if (!config.isParticlesEnabled()) {
            return;
        }

        try {
            Particle particle = Particle.valueOf(config.getParticleType());
            Location centerLocation = location.clone().add(0.5, 0.5, 0.5);

            location.getWorld().spawnParticle(
                    particle,
                    centerLocation,
                    config.getParticleAmount(),
                    config.getParticleOffsetX(),
                    config.getParticleOffsetY(),
                    config.getParticleOffsetZ()
            );
        } catch (IllegalArgumentException e) {
        }
    }

    private void playSound(Player player, Location location) {
        if (!config.isSoundEnabled()) {
            return;
        }

        try {
            Sound sound = Sound.valueOf(config.getSoundType());
            player.playSound(
                    location,
                    sound,
                    config.getSoundVolume(),
                    config.getSoundPitch()
            );
        } catch (IllegalArgumentException e) {
        }
    }

    private void showActionBar(Player player, int remainingUses) {
        if (!config.isActionbarEnabled()) {
            return;
        }

        messageUtil.sendActionBar(player, "spawner-broken-actionbar",
                "{uses}", String.valueOf(remainingUses));
    }
}
