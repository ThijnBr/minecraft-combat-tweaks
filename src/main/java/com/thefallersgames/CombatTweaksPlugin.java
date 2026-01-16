package com.thefallersgames;

import com.thefallersgames.commands.CombatTweaksCommand;
import com.thefallersgames.config.ConfigManager;
import com.thefallersgames.listeners.CrossbowRocketListener;
import com.thefallersgames.listeners.ShieldKnockbackListener;
import com.thefallersgames.listeners.SplashPotionListener;
import com.thefallersgames.listeners.TippedArrowListener;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatTweaksPlugin extends JavaPlugin {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Initialize configuration
        configManager = new ConfigManager(this);

        // Register event listeners
        registerListeners();

        // Register commands
        registerCommands();

        // Plugin startup message
        getLogger().info("=================================");
        getLogger().info("Thijns Combat Tweaks v" + getDescription().getVersion());
        getLogger().info("Plugin enabled successfully!");
        getLogger().info("=================================");

        if (configManager.isDebugMode()) {
            getLogger().info("[DEBUG] Debug mode is ENABLED");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Thijns Combat Tweaks disabled. Thanks for using!");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new CrossbowRocketListener(this), this);
        getServer().getPluginManager().registerEvents(new TippedArrowListener(this), this);
        getServer().getPluginManager().registerEvents(new SplashPotionListener(this), this);
        getServer().getPluginManager().registerEvents(new ShieldKnockbackListener(this), this);

        if (configManager.isDebugMode()) {
            getLogger().info("[DEBUG] All event listeners registered");
        }
    }

    private void registerCommands() {
        CombatTweaksCommand commandExecutor = new CombatTweaksCommand(this);
        getCommand("combattweaks").setExecutor(commandExecutor);
        getCommand("combattweaks").setTabCompleter(commandExecutor);

        if (configManager.isDebugMode()) {
            getLogger().info("[DEBUG] Commands registered");
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
