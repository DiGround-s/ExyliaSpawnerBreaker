package net.exylia.exyliaSpawnerBreaker.command.subcommands;

import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class HelpCommand {

    @Command({"spawnerbreaker", "sb", "sbreaker"})
    @CommandPermission("spawnerbreaker.help")
    public void execute(CommandSender sender) {
        sender.sendMessage("§b§lExyliaSpawnerBreaker §7- §fComandos:");
        sender.sendMessage("§7/spawnerbreaker give <player> <tipo> [usos] §f- §7Dar pico especial");
        sender.sendMessage("§7/spawnerbreaker reload §f- §7Recargar configuración");
        sender.sendMessage("§7/spawnerbreaker types §f- §7Listar tipos de picos");
    }
}
