package com.github.r0306.AntiSprint;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import java.util.List;

public class SprintListener implements Listener {

	private final AntiSprint plugin;

	public SprintListener(AntiSprint plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onSprint(PlayerToggleSprintEvent event) {
		if (plugin.getConfig().getBoolean("Enabled")) {
			Player player = event.getPlayer();
			int food = player.getFoodLevel();
			int minFood = plugin.getConfig().getInt("Minimum-Food");
			List<String> allowedSprintList = plugin.getConfig().getStringList("Allow-Sprint");

			// Debug-Ausgaben
			plugin.getLogger().info("Player: " + player.getName());
			plugin.getLogger().info("Food Level: " + food);
			plugin.getLogger().info("Minimum Food: " + minFood);
			plugin.getLogger().info("Allow Sprint List: " + allowedSprintList);
			plugin.getLogger().info("Is Player OP: " + player.isOp());
			plugin.getLogger().info("Player Has Permission 'antisprint.bypass': " + player.hasPermission("antisprint.bypass"));

			// Überprüfen, ob der Spieler in der Liste der erlaubten Sprinten ist oder eine Berechtigung hat
			if (!allowedSprintList.contains(player.getName()) && !player.hasPermission("antisprint.bypass")) {
				if (event.isSprinting() && food <= minFood) {
					// Setzt die Nahrung auf ein Minimum und cancelt das Sprinten
					player.setFoodLevel(1);
					event.setCancelled(true);

					// Setzt die Nahrung nach einer kurzen Verzögerung wieder auf den ursprünglichen Wert
					Bukkit.getScheduler().runTask(plugin, () -> player.setFoodLevel(food));
					plugin.getLogger().info("Sprint cancelled for player: " + player.getName());
				}
			} else {
				plugin.getLogger().info("Sprint not cancelled for player: " + player.getName() + " - either in allow list or has bypass permission.");
			}
		}
	}
}
