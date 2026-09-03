package com.kusokmedi.donateduration;

import org.bukkit.ChatColor;

public class ChatColorUtil {
    /**
     * Конвертирует & коды в ChatColor коды
     * Работает с версиями 1.16+
     */
    public static String translateColorCodes(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Удаляет все цветовые коды
     */
    public static String stripColorCodes(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }
        return ChatColor.stripColor(message);
    }
}
