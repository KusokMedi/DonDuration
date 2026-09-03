package com.kusokmedi.donateduration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

/**
 * Автозаполнение команд для DonateDuration
 * Предоставляет список доступных подкоманд
 */
public class DurationTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        // Проверяем первый аргумент команды
        if (args.length == 1) {
            String partialArg = args[0].toLowerCase();
            
            // Добавляем подкоманду "reload"
            if ("reload".startsWith(partialArg) && sender.hasPermission("donateduration.reload")) {
                completions.add("reload");
            }
            
            // Добавляем подкоманду "help"
            if ("help".startsWith(partialArg)) {
                completions.add("help");
            }
        }
        
        return completions;
    }
}
