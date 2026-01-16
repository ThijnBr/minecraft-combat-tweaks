# Thijns Combat Tweaks

A Minecraft plugin that enhances combat mechanics with crossbow rockets, tipped arrows, and splash potions for more dynamic PvP experiences.

## Features

### 🎯 Enhanced Crossbow Rockets
- Firework rockets fired from crossbows deal enhanced damage similar to charged creepers
- Configurable damage multiplier for balanced gameplay (default: 1.5x)
- Maintains vanilla crossbow mechanics while adding strategic depth

### 🏹 Tipped Arrow Armor Integration
- Tipped arrows now respect armor calculations
- Instant Damage arrows are reduced by armor, making protection valuable
- Configurable armor effectiveness against potion effects

### 💥 Splash Potion Balancing
- Optional armor calculation for splash and lingering potions
- Prevents splash potions from completely bypassing armor
- Maintains strategic value of armor in PvP scenarios

### 🛡️ Shield Knockback
- Knockback enchantments still work when holding shield up
- Prevents knockback enchantments from being completely useless
- Configurable knockback factor (default: 0.5 = half knockback)

## Installation

1. Download the latest release JAR file
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin using the generated `config.yml` file

## Requirements

- Paper/Spigot server 1.21.4
- Java 21 or higher

## Configuration

The plugin generates a `config.yml` file with the following sections:

### Crossbow Settings
```yaml
crossbow:
  firework_rocket:
    enhanced_damage_enabled: true
    damage_factor: 1.5  # Damage multiplier (1.0-2.5 recommended)
```

### Tipped Arrow Settings
```yaml
tipped_arrows:
  enforce_armor_calculation: true
  potion_effect_armor_factor: 1.0  # Armor effectiveness multiplier
```

### Splash Potion Settings
```yaml
splash_potions:
  enforce_armor_calculation: false
  potion_effect_armor_factor: 1.0
```

### Shield Knockback Settings
```yaml
shield:
  knockback_through_shield: true
  knockback_factor: 0.5  # Knockback when blocking
```

### Debug Settings
```yaml
debug_mode: false  # Enable detailed console logging
```

## Commands

- `/combattweaks reload` - Reload the plugin configuration
- `/combattweaks debug` - Toggle debug mode
- `/combattweaks help` - Show command help

## Permissions

- `combattweaks.use` - Basic command usage (default: true)
- `combattweaks.reload` - Reload configuration (default: op)
- `combattweaks.debug` - Toggle debug mode (default: op)

## Building from Source

```bash
mvn clean package
```

The compiled JAR will be in `target/ThijnsCombatTweaks-1.0.2.jar`

## License

This project is open source and available under the MIT License.

## Support

For issues, suggestions, or contributions, please open an issue on GitHub.

## Donations

If you enjoy this plugin, consider supporting development:
[PayPal](https://www.paypal.com/donate/?hosted_button_id=2KZ4AHLZHBV8A)
