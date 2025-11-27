package net.exylia.exyliaSpawnerBreaker.command.subcommands;

import com.google.inject.Inject;
import net.exylia.exyliaSpawnerBreaker.config.holder.PickaxeConfigData;
import net.exylia.exyliaSpawnerBreaker.service.PickaxeService;
import net.exylia.exyliaSpawnerBreaker.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class GiveCommand {

    private final PickaxeService pickaxeService;
    private final MessageUtil messageUtil;

    @Inject
    public GiveCommand(PickaxeService pickaxeService, MessageUtil messageUtil) {
        this.pickaxeService = pickaxeService;
        this.messageUtil = messageUtil;
    }

    @Command("spawnerbreaker give")
    @CommandPermission("spawnerbreaker.give")
    public void execute(
            CommandSender sender,
            Player target,
            @Suggest("pickaxe-types") String type,
            @Optional Integer uses
    ) {
        if (!pickaxeService.isValidType(type)) {
            messageUtil.send(sender, "invalid-type");
            return;
        }

        ItemStack pickaxe = pickaxeService.createPickaxe(type, uses);

        if (pickaxe == null) {
            messageUtil.send(sender, "invalid-type");
            return;
        }

        target.getInventory().addItem(pickaxe);

        PickaxeConfigData configData = pickaxeService.getPickaxeConfig(type);
        int finalUses = uses != null ? uses : (configData != null ? configData.getDefaultUses() : 0);

        messageUtil.send(target, "pickaxe-received",
                "{type}", type,
                "{uses}", String.valueOf(finalUses)
        );

        if (!sender.equals(target)) {
            sender.sendMessage("§aHas dado un pico §b" + type + " §acon §e" + finalUses + " §ausos a §f" + target.getName());
        }
    }
}
