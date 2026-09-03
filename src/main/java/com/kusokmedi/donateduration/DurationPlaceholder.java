package com.kusokmedi.donateduration;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.luckperms.api.LuckPerms;
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
    private final LuckPerms luckPerms;
    private final Map<UUID, CachedResult> cache = new ConcurrentHashMap<>();
    
    // Неизменяемое имя плейсхолдера
    private static final String PLACEHOLDER_ID = "donduration";
    
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

    public DurationPlaceholder(DonateDuration plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        // Плейсхолдер всегда %donduration%, конфиг ignored
        return PLACEHOLDER_ID;
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
                    plugin.getLogger().info("[Cache HIT] " + player.getName());
                }
                return cached.value;
            }
        }

        long duration = getDonateTime(player);
        String result;
        
        if (duration < 0) {
            // Бесконечная группа
            String infinitySymbol = plugin.getConfig().getString("infinity-symbol", "∞");
            result = ChatColorUtil.translateColorCodes(infinitySymbol);
        } else if (duration <= 0) {
            result = "0";
        } else {
            String format = plugin.getConfig().getString("placeholder", "%duration%%letter%");
            String[] parts = calculateDuration(duration);
            result = ChatColorUtil.translateColorCodes(format)
                    .replace("%duration%", parts[0])
                    .replace("%letter%", parts[1]);
        }

        // Сохранение в кэш
        if (cacheEnabled) {
            long ttl = plugin.getConfig().getLong("cache.ttl", 60) * 1000;
            cache.put(player.getUniqueId(), new CachedResult(result, System.currentTimeMillis() + ttl));
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("[Cache MISS] " + player.getName() + " => " + result);
            }
        }
        
        return result;
    }

    public void clearCache() {
        cache.clear();
        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Cache cleared!");
        }
    }

    public void cleanupExpiredCache() {
        int removed = 0;
        for (var iterator = cache.entrySet().iterator(); iterator.hasNext(); ) {
            var entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
                removed++;
            }
        }
        
        // Проверяем максимальный размер
        int maxSize = plugin.getConfig().getInt("cache.max-size", 1000);
        if (cache.size() > maxSize) {
            cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        }
        
        if (plugin.getConfig().getBoolean("debug", false) && removed > 0) {
            plugin.getLogger().info("Cleaned up " + removed + " expired cache entries");
        }
    }

    private long getDonateTime(OfflinePlayer player) {
        try {
            if (player == null || luckPerms == null) {
                return 0;
            }

            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            
            if (user == null) {
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().warning("[LP] User not found: " + player.getName());
                }
                return 0;
            }
            
            String primaryGroup = user.getPrimaryGroup();
            
            if (primaryGroup == null || primaryGroup.isEmpty()) {
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().warning("[LP] No primary group: " + player.getName());
                }
                return 0;
            }
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("[LP] Primary group: " + player.getName() + " => " + primaryGroup);
            }
            
            // Ищем ноду с этой группой
            for (Node node : user.getNodes()) {
                if (node.getKey().equals("group." + primaryGroup)) {
                    if (node.hasExpiry()) {
                        Instant expiry = node.getExpiry();
                        if (expiry != null) {
                            long seconds = Duration.between(Instant.now(), expiry).getSeconds();
                            long result = Math.max(0, seconds);
                            
                            if (plugin.getConfig().getBoolean("debug", false)) {
                                plugin.getLogger().info("[LP] Expiry: " + player.getName() + " => " + result + "s");
                            }
                            
                            return result;
                        }
                    } else {
                        // Группа без срока действия - бесконечность
                        if (plugin.getConfig().getBoolean("debug", false)) {
                            plugin.getLogger().info("[LP] Permanent group: " + player.getName());
                        }
                        return -1;
                    }
                }
            }
            
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().warning("[LP] Group node not found: " + player.getName() + " => " + primaryGroup);
            }
            return 0;
        } catch (Exception e) {
            plugin.getLogger().warning("[ERROR] Getting donate time for " + player.getName() + ": " + e.getMessage());
            if (plugin.getConfig().getBoolean("debug", false)) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    private String[] calculateDuration(long seconds) {
        if (seconds <= 0) {
            return new String[]{"0", plugin.getConfig().getString("letters.seconds", "сек")};
        }

        final long YEAR_SECONDS = 365L * 24 * 3600;
        final long MONTH_SECONDS = 30L * 24 * 3600;
        final long DAY_SECONDS = 24L * 3600;
        final long HOUR_SECONDS = 3600L;
        final long MINUTE_SECONDS = 60L;

        if (seconds >= YEAR_SECONDS) {
            long years = seconds / YEAR_SECONDS;
            return new String[]{String.valueOf(years), plugin.getConfig().getString("letters.years", "г")};
        }
        
        if (seconds >= MONTH_SECONDS) {
            long months = seconds / MONTH_SECONDS;
            return new String[]{String.valueOf(months), plugin.getConfig().getString("letters.months", "мес")};
        }
        
        if (seconds >= DAY_SECONDS) {
            long days = seconds / DAY_SECONDS;
            return new String[]{String.valueOf(days), plugin.getConfig().getString("letters.days", "д")};
        }
        
        if (seconds >= HOUR_SECONDS) {
            long hours = seconds / HOUR_SECONDS;
            return new String[]{String.valueOf(hours), plugin.getConfig().getString("letters.hours", "час")};
        }
        
        if (seconds >= MINUTE_SECONDS) {
            long minutes = seconds / MINUTE_SECONDS;
            return new String[]{String.valueOf(minutes), plugin.getConfig().getString("letters.minutes", "мин")};
        }
        
        return new String[]{String.valueOf(seconds), plugin.getConfig().getString("letters.seconds", "сек")};
    }
}
