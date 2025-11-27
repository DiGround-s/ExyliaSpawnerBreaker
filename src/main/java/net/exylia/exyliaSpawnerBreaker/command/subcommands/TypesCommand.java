package net.exylia.exyliaSpawnerBreaker.command.subcommands;

import com.google.inject.Inject;
import net.exylia.exyliaSpawnerBreaker.config.holder.PickaxeConfigData;
import net.exylia.exyliaSpawnerBreaker.service.PickaxeService;
import net.exylia.exyliaSpawnerBreaker.util.MessageUtil;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Set;

public class TypesCommand {

    private final PickaxeService pickaxeService;
    private final MessageUtil messageUtil;

    @Inject
    public TypesCommand(PickaxeService pickaxeService, MessageUtil messageUtil) {
        this.pickaxeService = pickaxeService;
        this.messageUtil = messageUtil;
    }

    @Command("spawnerbreaker types")
    @CommandPermission("spawnerbreaker.types")
    public void execute(CommandSender sender) {
        messageUtil.send(sender, "types-header");

        Set<String> types = pickaxeService.getAvailableTypes();

        for (String type : types) {
            PickaxeConfigData configData = pickaxeService.getPickaxeConfig(type);
            if (configData != null) {
                messageUtil.send(sender, "types-format",
                        "{type}", type,
                        "{uses}", String.valueOf(configData.getDefaultUses())
                );
            }
        }
    }
}
