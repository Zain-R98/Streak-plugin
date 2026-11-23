package dev.zain.streakplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StreakPlugin extends JavaPlugin implements Listener {

    private final Map<UUID, Integer> currentStreaks = new HashMap<>();
    private final Map<UUID, Integer> bestStreaks = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("StreakPlugin enabled successfully.");
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        UUID id = killer.getUniqueId();
        int streak = currentStreaks.getOrDefault(id, 0) + 1;
        currentStreaks.put(id, streak);

        if (streak > bestStreaks.getOrDefault(id, 0)) {
            bestStreaks.put(id, streak);
        }

        if (streak % 5 == 0) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[Streak] " + ChatColor.WHITE
                    + killer.getName() + ChatColor.YELLOW + " is on a " + streak + "-kill streak!");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID id = player.getUniqueId();

        int lostStreak = currentStreaks.getOrDefault(id, 0);
        currentStreaks.put(id, 0);

        if (!event.getDrops().isEmpty()) {
            Location loc = player.getLocation().getBlock().getLocation();
            Block block = loc.getBlock();
            block.setType(Material.CHEST);

            if (block.getState() instanceof Chest chest) {
                for (ItemStack item : event.getDrops()) {
                    if (item != null && item.getType() != Material.AIR) {
                        chest.getInventory().addItem(item);
                    }
                }
                event.getDrops().clear();
                player.sendMessage(ChatColor.AQUA + "Death Chest spawned at X:" + loc.getBlockX()
                        + " Y:" + loc.getBlockY() + " Z:" + loc.getBlockZ()
                        + (lostStreak > 0 ? " (Lost streak: " + lostStreak + ")" : ""));
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("stats") && sender instanceof Player player) {
            UUID id = player.getUniqueId();
            int current = currentStreaks.getOrDefault(id, 0);
            int best = bestStreaks.getOrDefault(id, 0);
            player.sendMessage(ChatColor.GREEN + "--- Your Combat Stats ---");
            player.sendMessage(ChatColor.YELLOW + "Current Streak: " + ChatColor.WHITE + current);
            player.sendMessage(ChatColor.YELLOW + "Best Streak: " + ChatColor.WHITE + best);
            return true;
        }
        return false;
    }
}
