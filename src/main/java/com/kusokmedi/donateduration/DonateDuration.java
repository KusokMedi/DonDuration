package com.kusokmedi.donateduration;

import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class DonateDuration extends JavaPlugin {
    private FileConfiguration messagesConfig;
    private DurationPlaceholder placeholder;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadMessages();
        
        // Проверка LuckPerms
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            getLogger().severe("LuckPerms не найден! Плагин будет отключен.");
            getLogger().severe("Скачайте LuckPerms: https://luckperms.net/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholder = new DurationPlaceholder(this);
            placeholder.register();
            getLogger().info("PlaceholderAPI hook registered!");
        } else {
            getLogger().warning("PlaceholderAPI не найден! Placeholder не будет работать.");
        }
        
        getCommand("donateduration").setExecutor(new DurationCommand(this));
        getCommand("donateduration").setTabCompleter(new DurationTabCompleter());
        
        // bStats метрики
        int pluginId = 23744; // Необходимо будет заменить на реальный ID после регистрации на bStats
        Metrics metrics = new Metrics(this, pluginId);
        
        getLogger().info("DonateDuration v" + getDescription().getVersion() + " by KusokMedi enabled!");
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
        if (placeholder != null) {
            placeholder.clearCache();
        }
    }
}
