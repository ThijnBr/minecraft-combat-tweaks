package com.thefallersgames.commands;

import com.thefallersgames.CombatTweaksPlugin;
import com.thefallersgames.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombatTweaksCommand implements CommandExecutor, TabCompleter {
    private final CombatTweaksPlugin plugin;
    private final ConfigManager configManager;

    public CombatTweaksCommand(CombatTweaksPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                return handleReload(sender);
            case "debug":
                return handleDebug(sender);
            case "help":
                sendHelp(sender);
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /combattweaks help");
                return true;
        }
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("combattweaks.reload")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to reload the configuration.");
            return true;
        }

        try {
            configManager.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded successfully!");
            plugin.getLogger().info(sender.getName() + " reloaded the configuration");
            return true;
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Error reloading configuration: " + e.getMessage());
            plugin.getLogger().severe("Error reloading configuration: " + e.getMessage());
            return true;
        }
    }

    private boolean handleDebug(CommandSender sender) {
        if (!sender.hasPermission("combattweaks.debug")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to toggle debug mode.");
            return true;
        }

        boolean newDebugMode = !configManager.isDebugMode();
        configManager.setDebugMode(newDebugMode);

        String status = newDebugMode ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED";
        sender.sendMessage(ChatColor.YELLOW + "Debug mode " + status);
        plugin.getLogger().info(sender.getName() + " " + (newDebugMode ? "enabled" : "disabled") + " debug mode");

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Thijns Combat Tweaks ===");
        sender.sendMessage(ChatColor.YELLOW + "/combattweaks reload" + ChatColor.GRAY + " - Reload the configuration");
        sender.sendMessage(ChatColor.YELLOW + "/combattweaks debug" + ChatColor.GRAY + " - Toggle debug mode");
        sender.sendMessage(ChatColor.YELLOW + "/combattweaks help" + ChatColor.GRAY + " - Show this help message");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("reload", "debug", "help");
            String input = args[0].toLowerCase();

            for (String subCommand : subCommands) {
                if (subCommand.startsWith(input)) {
                    completions.add(subCommand);
                }
            }
        }

        return completions;
    }
}
