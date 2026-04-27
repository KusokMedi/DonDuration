package com.kusokmedi.donateduration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class DonateDuration extends JavaPlugin {
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadMessages();
        
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new DurationPlaceholder(this).register();
            getLogger().info("PlaceholderAPI hook registered!");
        }
        
        getCommand("donateduration").setExecutor(new DurationCommand(this));
        getCommand("donateduration").setTabCompleter(new DurationTabCompleter());
        getLogger().info("DonateDuration v6.7 by KusokMedi enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DonateDuration disabled!");
    }

    public void loadMessages() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(String key) {
        String message = messagesConfig.getString(key, "").replace("&", "§");
        String prefix = messagesConfig.getString("prefix", "").replace("&", "§");
        return message.replace("%prefix%", prefix);
    }

    public void reloadConfigs() {
        reloadConfig();
        loadMessages();
    }
}
