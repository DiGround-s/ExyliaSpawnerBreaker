package net.exylia.exyliaSpawnerBreaker.command.subcommands;

import com.google.inject.Inject;
import net.exylia.exyliaSpawnerBreaker.config.PickaxeConfig;
import net.exylia.exyliaSpawnerBreaker.config.cache.PickaxeConfigCache;
import net.exylia.exyliaSpawnerBreaker.util.MessageUtil;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class ReloadCommand {

    private final PickaxeConfig config;
    private final PickaxeConfigCache configCache;
    private final MessageUtil messageUtil;

    @Inject
    public ReloadCommand(PickaxeConfig config, PickaxeConfigCache configCache, MessageUtil messageUtil) {
        this.config = config;
        this.configCache = configCache;
        this.messageUtil = messageUtil;
    }

    @Command("spawnerbreaker reload")
    @CommandPermission("spawnerbreaker.reload")
    public void execute(CommandSender sender) {
        try {
            config.reload();
            configCache.invalidateAll();
            messageUtil.send(sender, "reload-success");
        } catch (Exception e) {
            messageUtil.send(sender, "reload-error");
            e.printStackTrace();
        }
    }
}
