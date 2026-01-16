package com.thefallersgames.listeners;

import com.thefallersgames.CombatTweaksPlugin;
import com.thefallersgames.config.ConfigManager;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class CrossbowRocketListener implements Listener {
    private final CombatTweaksPlugin plugin;
    private final ConfigManager configManager;

    public CrossbowRocketListener(CombatTweaksPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFireworkDamage(EntityDamageByEntityEvent event) {
        // Check if enhanced damage is enabled
        if (!configManager.isCrossbowEnhancedDamageEnabled()) {
            return;
        }

        // Check if damager is a firework
        if (!(event.getDamager() instanceof Firework)) {
            return;
        }

        Firework firework = (Firework) event.getDamager();

        // Check if the firework was shot from a crossbow
        // Fireworks shot from crossbows have a shooter set
        ProjectileSource shooter = firework.getShooter();
        if (shooter == null) {
            return;
        }

        // Get damage factor from config
        double damageFactor = configManager.getCrossbowDamageFactor();

        // Apply enhanced damage
        double originalDamage = event.getDamage();
        double newDamage = originalDamage * damageFactor;
        event.setDamage(newDamage);

        if (configManager.isDebugMode()) {
            plugin.getLogger().info(String.format(
                    "[DEBUG] Crossbow rocket hit %s - Original: %.2f, Enhanced: %.2f (x%.2f)",
                    event.getEntity().getName(),
                    originalDamage,
                    newDamage,
                    damageFactor));
        }
    }
}
