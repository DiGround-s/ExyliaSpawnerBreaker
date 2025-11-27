package net.exylia.exyliaSpawnerBreaker.command;

import com.google.inject.Injector;
import lombok.Getter;
import net.exylia.exyliaSpawnerBreaker.ExyliaSpawnerBreaker;
import net.exylia.exyliaSpawnerBreaker.command.subcommands.GiveCommand;
import net.exylia.exyliaSpawnerBreaker.command.subcommands.HelpCommand;
import net.exylia.exyliaSpawnerBreaker.command.subcommands.ReloadCommand;
import net.exylia.exyliaSpawnerBreaker.command.subcommands.TypesCommand;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public class SpawnerBreakerCommand {
    private final ExyliaSpawnerBreaker plugin;
    @Getter
    private final Lamp<BukkitCommandActor> lamp;

    public SpawnerBreakerCommand(ExyliaSpawnerBreaker plugin, Injector injector) {
        this.plugin = plugin;
        this.lamp = BukkitLamp.builder(plugin).build();

        registerCommands(injector);
    }

    private void registerCommands(Injector injector) {
        lamp.register(injector.getInstance(HelpCommand.class));
        lamp.register(injector.getInstance(GiveCommand.class));
        lamp.register(injector.getInstance(ReloadCommand.class));
        lamp.register(injector.getInstance(TypesCommand.class));
    }

}
