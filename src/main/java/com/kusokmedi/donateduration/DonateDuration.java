package com.kusokmedi.donateduration;

import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import java.io.File;

public class DonateDuration extends JavaPlugin {
    private FileConfiguration messagesConfig;
    private DurationPlaceholder placeholder;
    private LuckPerms luckPerms;
    private BukkitTask cacheCleanupTask;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadMessages();
        validateConfig();
        
        // Проверка LuckPerms
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            getLogger().severe("LuckPerms не найден! Плагин будет отключен.");
            getLogger().severe("Скачайте LuckPerms: https://luckperms.net/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // Кэшируем LuckPerms API
        try {
            this.luckPerms = LuckPermsProvider.get();
        } catch (IllegalStateException e) {
            getLogger().severe("Не удалось загрузить LuckPerms API!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholder = new DurationPlaceholder(this, luckPerms);
            placeholder.register();
            getLogger().info("PlaceholderAPI hook registered!");
        } else {
            getLogger().warning("PlaceholderAPI не найден! Placeholder не будет работать.");
        }
        
        // Регистрируем команды
        if (getCommand("donateduration") != null) {
            getCommand("donateduration").setExecutor(new DurationCommand(this));
            getCommand("donateduration").setTabCompleter(new DurationTabCompleter());
        }
        
        // Запускаем task для очистки кэша
        startCacheCleanupTask();
        
        // bStats метрики
        int pluginId = 23744;
        new Metrics(this, pluginId);
        
        getLogger().info("DonateDuration v" + getDescription().getVersion() + " by KusokMedi enabled!");
    }

    @Override
    public void onDisable() {
        if (cacheCleanupTask != null) {
            cacheCleanupTask.cancel();
        }
        if (placeholder != null) {
            placeholder.clearCache();
        }
        getLogger().info("DonateDuration disabled!");
    }

    private void startCacheCleanupTask() {
        long cleanupInterval = getConfig().getLong("cache.cleanup-interval", 300) * 20L; // В тиках
        if (cleanupInterval <= 0) cleanupInterval = 300 * 20L;
        
        cacheCleanupTask = getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (placeholder != null) {
                placeholder.cleanupExpiredCache();
                if (getConfig().getBoolean("debug", false)) {
                    getLogger().info("Cache cleanup executed");
                }
            }
        }, cleanupInterval, cleanupInterval);
    }

    private void validateConfig() {
        // Проверяем наличие всех необходимых ключей
        if (!getConfig().contains("letters.seconds")) {
            getConfig().set("letters.seconds", "сек");
        }
        if (!getConfig().contains("letters.minutes")) {
            getConfig().set("letters.minutes", "мин");
        }
        if (!getConfig().contains("letters.hours")) {
            getConfig().set("letters.hours", "час");
        }
        if (!getConfig().contains("letters.days")) {
            getConfig().set("letters.days", "д");
        }
        if (!getConfig().contains("letters.months")) {
            getConfig().set("letters.months", "мес");
        }
        if (!getConfig().contains("letters.years")) {
            getConfig().set("letters.years", "г");
        }
        if (!getConfig().contains("placeholder")) {
            getConfig().set("placeholder", "%duration%%letter%");
        }
    }

    public void loadMessages() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(String key) {
        String message = messagesConfig.getString(key, "");
        String prefix = messagesConfig.getString("prefix", "");
        return ChatColorUtil.translateColorCodes(message.replace("%prefix%", prefix));
    }

    public void reloadConfigs() {
        reloadConfig();
        loadMessages();
        validateConfig();
        if (placeholder != null) {
            placeholder.clearCache();
        }
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
