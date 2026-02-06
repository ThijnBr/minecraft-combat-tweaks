package com.thefallersgames.listeners;

import com.thefallersgames.CombatTweaksPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashSet;
import java.util.Set;

public class SpearAttributeListener implements Listener {

    private final CombatTweaksPlugin plugin;
    private final Set<Material> spearMaterials;

    public SpearAttributeListener(CombatTweaksPlugin plugin) {
        this.plugin = plugin;

        // All spear types - use dynamic loading for forward compatibility
        this.spearMaterials = new HashSet<>();
        String[] spearNames = {
                "TRIDENT", // Vanilla trident
                "WOODEN_SPEAR", "STONE_SPEAR", "IRON_SPEAR",
                "GOLDEN_SPEAR", "DIAMOND_SPEAR", "NETHERITE_SPEAR",
                "COPPER_SPEAR"
        };

        for (String spearName : spearNames) {
            try {
                Material spear = Material.valueOf(spearName);
                spearMaterials.add(spear);
            } catch (IllegalArgumentException e) {
                // Material doesn't exist in this version
            }
        }
    }

    /**
     * Check if a material is a spear type
     */
    public boolean isSpear(Material material) {
        return spearMaterials.contains(material);
    }

    /**
     * Check if the usage of this item is disabled by config
     */
    private boolean isUsageDisabled(Material material) {
        if (!spearMaterials.contains(material)) {
            return false; // Not a spear/trident, so not disabled by this logic
        }

        if (material == Material.TRIDENT) {
            return !plugin.getConfigManager().isTridentsEnabled();
        } else {
            return !plugin.getConfigManager().isSpearsEnabled();
        }
    }

    /**
     * Handle interaction blocking
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack weapon = event.getItem();

        if (weapon == null) {
            return;
        }

        if (isUsageDisabled(weapon.getType())) {
            event.setCancelled(true);
            weapon.setAmount(0); // Remove the item from inventory
        }
    }

    /**
     * Main damage handler - Block attacks if item is disabled
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Check if attacker is a player
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        ItemStack weapon = player.getInventory().getItemInMainHand();

        // Check if usage is disabled
        if (weapon != null && isUsageDisabled(weapon.getType())) {
            event.setCancelled(true);
            weapon.setAmount(0); // Remove the item from inventory
        }
    }

    /**
     * Remove item when holding it if disabled
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onViewItem(org.bukkit.event.player.PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (item != null && isUsageDisabled(item.getType())) {
            item.setAmount(0); // Remove the item from inventory
        }
    }

    /**
     * Remove item when pickup if disabled
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPickupItem(org.bukkit.event.entity.EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        ItemStack item = event.getItem().getItemStack();
        if (isUsageDisabled(item.getType())) {
            event.setCancelled(true);
            event.getItem().remove(); // Remove the item entity from the world
        }
    }
}
