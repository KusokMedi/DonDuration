package com.kusokmedi.donateduration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DurationCommand implements CommandExecutor {
    private final DonateDuration plugin;

    public DurationCommand(DonateDuration plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "reload":
                handleReload(sender);
                return true;
            case "help":
            case "?":
                sendHelpMessage(sender);
                return true;
            default:
                sender.sendMessage(plugin.getMessage("unknown-command"));
                sendHelpMessage(sender);
                return true;
        }
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("donateduration.reload")) {
            sender.sendMessage(plugin.getMessage("no-permission"));
            return;
        }

        try {
            plugin.reloadConfigs();
            sender.sendMessage(plugin.getMessage("reload-success"));
        } catch (Exception e) {
            sender.sendMessage(plugin.getMessage("reload-error"));
            plugin.getLogger().warning("Error reloading config: " + e.getMessage());
            if (plugin.getConfig().getBoolean("debug", false)) {
                e.printStackTrace();
            }
        }
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(plugin.getMessage("prefix") + " &eДоступные команды:");
        sender.sendMessage("  &6/dd reload &7- Перезагрузить конфигурацию");
        sender.sendMessage("  &6/dd help &7- Показать эту справку");
    }
}
