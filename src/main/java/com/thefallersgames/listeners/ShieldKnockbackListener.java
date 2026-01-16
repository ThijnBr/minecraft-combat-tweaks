package com.thefallersgames.listeners;

import com.thefallersgames.CombatTweaksPlugin;
import com.thefallersgames.config.ConfigManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ShieldKnockbackListener implements Listener {
    private final CombatTweaksPlugin plugin;
    private final ConfigManager configManager;

    public ShieldKnockbackListener(CombatTweaksPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onShieldBlock(EntityDamageByEntityEvent event) {
        // Check if shield knockback is enabled
        if (!configManager.isShieldKnockbackEnabled()) {
            return;
        }

        // Check if victim is a living entity blocking with a shield
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity victim = (LivingEntity) event.getEntity();

        // Check if victim is actively using an item (blocking)
        if (!victim.isHandRaised()) {
            return;
        }

        // Check if the item being used is a shield
        ItemStack activeItem = victim.getActiveItem();
        if (activeItem == null || activeItem.getType() != Material.SHIELD) {
            return;
        }

        // Check if attacker is a living entity
        if (!(event.getDamager() instanceof LivingEntity)) {
            return;
        }

        LivingEntity attacker = (LivingEntity) event.getDamager();
        ItemStack weapon = attacker.getEquipment() != null ? attacker.getEquipment().getItemInMainHand() : null;

        if (weapon == null || !weapon.hasItemMeta()) {
            return;
        }

        // Check for knockback enchantment
        int knockbackLevel = weapon.getEnchantmentLevel(Enchantment.KNOCKBACK);

        if (knockbackLevel <= 0) {
            return;
        }

        // Apply knockback even though shield is blocking
        double knockbackFactor = configManager.getShieldKnockbackFactor();

        // Calculate knockback direction (away from attacker)
        Vector direction = victim.getLocation().toVector()
                .subtract(attacker.getLocation().toVector())
                .normalize();

        // Apply knockback (0.4 per level is base Minecraft knockback)
        double knockbackStrength = knockbackLevel * 0.4 * knockbackFactor;
        direction.multiply(knockbackStrength);
        direction.setY(0.2); // Add slight upward motion

        victim.setVelocity(direction);

        if (configManager.isDebugMode()) {
            plugin.getLogger().info(String.format(
                    "[DEBUG] Shield knockback: %s hit %s (level %d, factor %.2f)",
                    attacker.getName(),
                    victim.getName(),
                    knockbackLevel,
                    knockbackFactor));
        }
    }
}
