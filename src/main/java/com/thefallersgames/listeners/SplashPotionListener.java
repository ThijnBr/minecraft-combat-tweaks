package com.thefallersgames.listeners;

import com.thefallersgames.CombatTweaksPlugin;
import com.thefallersgames.config.ConfigManager;
import com.thefallersgames.utils.DamageCalculator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SplashPotionListener implements Listener {
    private final CombatTweaksPlugin plugin;
    private final ConfigManager configManager;

    public SplashPotionListener(CombatTweaksPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPotionSplash(PotionSplashEvent event) {
        // Check if armor calculation is enabled
        if (!configManager.isSplashPotionArmorCalculationEnabled()) {
            return;
        }

        ThrownPotion potion = event.getPotion();
        double armorFactor = configManager.getSplashPotionArmorFactor();

        // Process each affected entity
        event.getAffectedEntities().forEach(entity -> {
            if (!(entity instanceof LivingEntity)) {
                return;
            }

            LivingEntity livingEntity = (LivingEntity) entity;
            double armor = DamageCalculator.getArmorPoints(livingEntity);
            double intensity = event.getIntensity(livingEntity);

            if (configManager.isDebugMode()) {
                plugin.getLogger().info(String.format(
                        "[DEBUG] Splash potion affecting %s (armor: %.2f, intensity: %.2f)",
                        livingEntity.getName(),
                        armor,
                        intensity));
            }

            // Modify intensity based on armor
            if (armor > 0) {
                double reduction = DamageCalculator.calculateArmorReduction(armor) * armorFactor;
                double newIntensity = intensity * (1.0 - reduction);
                event.setIntensity(livingEntity, newIntensity);

                if (configManager.isDebugMode()) {
                    plugin.getLogger().info(String.format(
                            "[DEBUG] Reduced intensity from %.2f to %.2f (%.0f%% reduction)",
                            intensity,
                            newIntensity,
                            reduction * 100));
                }
            }
        });
    }
}
