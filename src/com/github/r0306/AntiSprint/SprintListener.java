package com.github.r0306.AntiSprint;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class SprintListener implements Listener
{
	
	private AntiSprint plugin;
	public SprintListener(AntiSprint plugin)
	{
		
		this.plugin = plugin;
		
	}

	@EventHandler (ignoreCancelled = true)
	public void onSprint(PlayerToggleSprintEvent event)
	{
		
		if (plugin.getConfig().getBoolean("Enabled"))
		{
		
			final Player player = event.getPlayer();
			final int food = player.getFoodLevel();
			
			if (!plugin.getConfig().getStringList("Allow-Sprint").contains(player.getName()) && !player.hasPermission("antisprint.bypass"))
			{
			
				if (event.isSprinting())
				{
				
					if (food <= plugin.getConfig().getInt("Minimum-Food"))
					{
						
						player.setFoodLevel(1);
						event.setCancelled(true);
					
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
						{
							   			
							   public void run()
							   {
								
								   player.setFoodLevel(food);
								   
							   }
							
						}, 0L);
						
					}
					
				}
				
			}
		
		}
	
	}
	
}
