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
            sender.sendMessage(plugin.getMessage("usage"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("donateduration.reload")) {
                sender.sendMessage(plugin.getMessage("no-permission"));
                return true;
            }
            plugin.reloadConfigs();
            sender.sendMessage(plugin.getMessage("reload-success"));
            return true;
        }

        sender.sendMessage(plugin.getMessage("usage"));
        return true;
    }
}
