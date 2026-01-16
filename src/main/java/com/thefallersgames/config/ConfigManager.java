package com.thefallersgames.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    // Debug mode
    public boolean isDebugMode() {
        return config.getBoolean("debug_mode", false);
    }

    public void setDebugMode(boolean value) {
        config.set("debug_mode", value);
        plugin.saveConfig();
    }

    // Crossbow settings
    public boolean isCrossbowEnhancedDamageEnabled() {
        return config.getBoolean("crossbow.firework_rocket.enhanced_damage_enabled", true);
    }

    public double getCrossbowDamageFactor() {
        double factor = config.getDouble("crossbow.firework_rocket.damage_factor", 1.5);
        // Clamp between reasonable values
        return Math.max(1.0, Math.min(2.5, factor));
    }

    // Tipped arrow settings
    public boolean isTippedArrowArmorCalculationEnabled() {
        return config.getBoolean("tipped_arrows.enforce_armor_calculation", true);
    }

    public double getTippedArrowArmorFactor() {
        return config.getDouble("tipped_arrows.potion_effect_armor_factor", 1.0);
    }

    // Splash potion settings
    public boolean isSplashPotionArmorCalculationEnabled() {
        return config.getBoolean("splash_potions.enforce_armor_calculation", false);
    }

    public double getSplashPotionArmorFactor() {
        return config.getDouble("splash_potions.potion_effect_armor_factor", 1.0);
    }

    // Shield knockback settings
    public boolean isShieldKnockbackEnabled() {
        return config.getBoolean("shield.knockback_through_shield", true);
    }

    public double getShieldKnockbackFactor() {
        return config.getDouble("shield.knockback_factor", 0.5);
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
