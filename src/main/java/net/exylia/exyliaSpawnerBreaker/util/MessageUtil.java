package net.exylia.exyliaSpawnerBreaker.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.exylia.exyliaSpawnerBreaker.config.PickaxeConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Singleton
public class MessageUtil {

    private final PickaxeConfig config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Inject
    public MessageUtil(PickaxeConfig config) {
        this.config = config;
    }

    public void send(CommandSender sender, String messageKey, String... replacements) {
        String message = config.getMessage(messageKey);
        if (message == null || message.isEmpty()) {
            return;
        }

        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        Component component = miniMessage.deserialize(message);
        sender.sendMessage(component);
    }

    public void sendActionBar(Player player, String messageKey, String... replacements) {
        String message = config.getMessage(messageKey);
        if (message == null || message.isEmpty()) {
            return;
        }

        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        Component component = miniMessage.deserialize(message);
        player.sendActionBar(component);
    }

    public String replace(String text, String... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                text = text.replace(replacements[i], replacements[i + 1]);
            }
        }
        return text;
    }
}
