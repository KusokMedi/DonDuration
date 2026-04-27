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

public class DurationPlaceholder extends PlaceholderExpansion {
    private final DonateDuration plugin;

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
        return "6.7";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        long duration = getDonateTime(player);
        
        if (duration < 0) {
            return plugin.getConfig().getString("infinity-symbol", "∞");
        }
        
        if (duration <= 0) return "0";

        String format = plugin.getConfig().getString("placeholder", "%duration%%letter%");
        String[] parts = calculateDuration(duration);
        
        return format.replace("%duration%", parts[0]).replace("%letter%", parts[1]);
    }

    private long getDonateTime(OfflinePlayer player) {
        try {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            
            if (user == null) {
                return 0;
            }
            
            // Получаем primary группу
            String primaryGroup = user.getPrimaryGroup();
            
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
            plugin.getLogger().warning("Error getting donate time: " + e.getMessage());
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
