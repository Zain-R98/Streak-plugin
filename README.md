# Minecraft Kill-Streak & DeathChest Plugin 🗡️

A basic Minecraft plugin built with Java and the Spigot/Paper API (1.21+) that tracks player combat streaks and protects dropped inventory items inside a spawned chest upon death.

## ✨ Features
- **Event-Driven Streak Tracking:** Uses `HashMap` structures keyed by player `UUID` to track active and personal-best kill streaks in $O(1)$ time.
- **Automated Death Chests:** Intercepts `PlayerDeathEvent` to store dropped `ItemStack` objects in a chest at the player's death coordinates.
- **Statistics:** `/stats` displays active and record streaks directly to the player.

## Build & Run
1. Download or Clone the repository and open it.
2. Build with Maven: `mvn clean package`
3. Put `target/StreakPlugin-1.1.2.jar` into your Paper server's `/plugins` folder.
4. Restart the server and run `/stats` to enjoy the plugin! 😀.

## License

This project is open-source and available under the [MIT License](LICENSE).