package com.thefallersgames.listeners;

import com.thefallersgames.CombatTweaksPlugin;
import com.thefallersgames.config.ConfigManager;
import com.thefallersgames.utils.DamageCalculator;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TippedArrowListener implements Listener {
    private final CombatTweaksPlugin plugin;
    private final ConfigManager configManager;

    public TippedArrowListener(CombatTweaksPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTippedArrowHit(EntityDamageByEntityEvent event) {
        // Check if armor calculation is enabled
        if (!configManager.isTippedArrowArmorCalculationEnabled()) {
            return;
        }

        // Check if the damager is an arrow and victim is a living entity
        if (!(event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        Arrow arrow = (Arrow) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

        // Check if the arrow has potion effects (tipped arrow)
        if (!arrow.hasCustomEffects()) {
            return;
        }

        // Get victim's armor
        double armor = DamageCalculator.getArmorPoints(victim);
        double armorFactor = configManager.getTippedArrowArmorFactor();

        if (configManager.isDebugMode()) {
            plugin.getLogger().info(String.format(
                    "[DEBUG] Tipped arrow hit %s with %.2f armor points",
                    victim.getName(),
                    armor));
        }

        // Modify each potion effect based on armor
        for (PotionEffect effect : arrow.getCustomEffects()) {
            PotionEffectType type = effect.getType();
            int duration = effect.getDuration();
            int amplifier = effect.getAmplifier();

            // For instant damage, reduce the amplifier based on armor
            if (type.equals(PotionEffectType.INSTANT_DAMAGE)) {
                // Calculate reduction
                double reduction = DamageCalculator.calculateArmorReduction(armor) * armorFactor;
                int newAmplifier = (int) Math.max(0, amplifier * (1.0 - reduction));

                if (configManager.isDebugMode()) {
                    plugin.getLogger().info(String.format(
                            "[DEBUG] Instant Damage: Original amplifier %d, Reduced to %d (%.0f%% reduction)",
                            amplifier,
                            newAmplifier,
                            reduction * 100));
                }

                // Remove original effect and apply modified one
                victim.removePotionEffect(type);
                if (newAmplifier > 0) {
                    victim.addPotionEffect(new PotionEffect(type, duration, newAmplifier,
                            effect.isAmbient(), effect.hasParticles(), effect.hasIcon()));
                }
            } else {
                // For other effects (poison, weakness, etc.), reduce duration or amplifier
                double reduction = DamageCalculator.calculateArmorReduction(armor) * armorFactor * 0.5;
                int newDuration = (int) Math.max(0, duration * (1.0 - reduction));

                if (configManager.isDebugMode() && reduction > 0) {
                    plugin.getLogger().info(String.format(
                            "[DEBUG] %s: Original duration %d, Reduced to %d",
                            type.getName(),
                            duration,
                            newDuration));
                }

                // Update the effect
                victim.removePotionEffect(type);
                if (newDuration > 0) {
                    victim.addPotionEffect(new PotionEffect(type, newDuration, amplifier,
                            effect.isAmbient(), effect.hasParticles(), effect.hasIcon()));
                }
            }
        }
    }
}
