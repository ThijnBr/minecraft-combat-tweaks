package com.thefallersgames.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class DamageCalculator {

    /**
     * Calculate the armor points of an entity
     * 
     * @param entity The entity to calculate armor for
     * @return Armor points (0-20)
     */
    public static double getArmorPoints(LivingEntity entity) {
        if (entity.getAttribute(Attribute.ARMOR) != null) {
            return entity.getAttribute(Attribute.ARMOR).getValue();
        }
        return 0.0;
    }

    /**
     * Calculate the armor toughness of an entity
     * 
     * @param entity The entity to calculate armor toughness for
     * @return Armor toughness value
     */
    public static double getArmorToughness(LivingEntity entity) {
        if (entity.getAttribute(Attribute.ARMOR_TOUGHNESS) != null) {
            return entity.getAttribute(Attribute.ARMOR_TOUGHNESS).getValue();
        }
        return 0.0;
    }

    /**
     * Calculate damage reduction based on armor
     * Formula from Minecraft Wiki: damage reduction = (armor * 0.04)
     * Capped at 80% reduction (armor value 20)
     * 
     * @param armor Armor points (0-20)
     * @return Damage reduction percentage (0.0-0.8)
     */
    public static double calculateArmorReduction(double armor) {
        // Each armor point reduces damage by 4%, max 80% at 20 armor
        double reduction = armor * 0.04;
        return Math.min(0.8, reduction);
    }

    /**
     * Calculate potion effect strength based on armor and configured factor
     * 
     * @param baseStrength Base potion effect strength
     * @param armor        Armor points
     * @param armorFactor  Configuration factor for armor effectiveness
     * @return Modified potion effect strength
     */
    public static double calculatePotionEffectWithArmor(double baseStrength, double armor, double armorFactor) {
        double reduction = calculateArmorReduction(armor) * armorFactor;
        return baseStrength * (1.0 - reduction);
    }

    /**
     * Calculate damage with armor reduction
     * 
     * @param baseDamage Base damage value
     * @param armor      Armor points
     * @return Damage after armor reduction
     */
    public static double calculateDamageWithArmor(double baseDamage, double armor) {
        double reduction = calculateArmorReduction(armor);
        return baseDamage * (1.0 - reduction);
    }

    /**
     * Calculate knockback velocity multiplier
     * 
     * @param knockbackLevel Knockback enchantment level
     * @param factor         Additional factor from configuration
     * @return Knockback multiplier
     */
    public static double calculateKnockbackMultiplier(int knockbackLevel, double factor) {
        // Base knockback is 0.4 per level in Minecraft
        return (knockbackLevel * 0.4) * factor;
    }
}
