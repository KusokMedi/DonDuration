package com.kusokmedi.donateduration;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DurationPlaceholder extends PlaceholderExpansion {
    private final DonateDuration plugin;
    private final Map<UUID, CachedResult> cache = new ConcurrentHashMap<>();
    
    private static class CachedResult {
        final String value;
        final long expireAt;
        
        CachedResult(String value, long expireAt) {
            this.value = value;
            this.expireAt = expireAt;
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() > expireAt;
        }
    }

    public DurationPlaceholder(DonateDuration plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return plugin.getConfig().getString("placeholder-name", "donateduration");
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "KusokMedi";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        // Проверка кэша
        boolean cacheEnabled = plugin.getConfig().getBoolean("cache.enabled", true);
        if (cacheEnabled) {
            CachedResult cached = cache.get(player.getUniqueId());
            if (cached != null && !cached.isExpired()) {
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info("Cache hit for player: " + player.getName());
                }
                return cached.value;
            }
        }

        long duration = getDonateTime(player);
        String result;
        
        if (duration < 0) {
            result = plugin.getConfig().getString("infinity-symbol", "∞");
        } else if (duration <= 0) {
            result = "0";
        } else {
            String format = plugin.getConfig().getString("placeholder", "%duration%%letter%");
            String[] parts = calculateDuration(duration);
            result = format.replace("%duration%", parts[0]).replace("%letter%", parts[1]);
        }

        // Сохранение в кэш
        if (cacheEnabled) {
            long ttl = plugin.getConfig().getLong("cache.ttl", 60) * 1000;
            cache.put(player.getUniqueId(), new CachedResult(result, System.currentTimeMillis() + ttl));
            
            // Очистка старых записей
            int maxSize = plugin.getConfig().getInt("cache.max-size", 1000);
            if (cache.size() > maxSize) {
                cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
            }
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("Cache miss for player: " + player.getName() + ", cached result: " + result);
            }
        }
        
        return result;
    }

    public void clearCache() {
        cache.clear();
        plugin.getLogger().info("Cache cleared!");
    }

    private long getDonateTime(OfflinePlayer player) {
        try {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            
            if (user == null) {
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().warning("User not found in LuckPerms: " + player.getName());
                }
                return 0;
            }
            
            // Получаем primary группу
            String primaryGroup = user.getPrimaryGroup();
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("Primary group for " + player.getName() + ": " + primaryGroup);
            }
            
            // Ищем ноду с этой группой
            for (Node node : user.getNodes()) {
                if (node.getKey().equals("group." + primaryGroup)) {
                    if (node.hasExpiry()) {
                        Instant expiry = node.getExpiry();
                        if (expiry != null) {
                            long seconds = Duration.between(Instant.now(), expiry).getSeconds();
                            return Math.max(0, seconds);
                        }
                    } else {
                        // Группа без срока действия - бесконечность
                        return -1;
                    }
                }
            }
            
            // Если primary группа не найдена или нет срока - 0
            return 0;
        } catch (Exception e) {
            plugin.getLogger().warning("Error getting donate time for " + player.getName() + ": " + e.getMessage());
            if (plugin.getConfig().getBoolean("debug", false)) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    private String[] calculateDuration(long seconds) {
        long years = seconds / 31536000;
        if (years > 0) return new String[]{String.valueOf(years), plugin.getConfig().getString("letters.years", "г")};
        
        long months = seconds / 2592000;
        if (months > 0) return new String[]{String.valueOf(months), plugin.getConfig().getString("letters.months", "мес")};
        
        long days = seconds / 86400;
        if (days > 0) return new String[]{String.valueOf(days), plugin.getConfig().getString("letters.days", "д")};
        
        long hours = seconds / 3600;
        if (hours > 0) return new String[]{String.valueOf(hours), plugin.getConfig().getString("letters.hours", "час")};
        
        long minutes = seconds / 60;
        if (minutes > 0) return new String[]{String.valueOf(minutes), plugin.getConfig().getString("letters.minutes", "мин")};
        
        return new String[]{String.valueOf(seconds), plugin.getConfig().getString("letters.seconds", "сек")};
    }
}
