package com.github.r0306.AntiSprint;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Executor implements CommandExecutor {

	private final AntiSprint plugin;

	public Executor(AntiSprint plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("antisprint")) {

			if (args.length == 0) {
				sender.sendMessage(ChatColor.GRAY + "This server is running Anti-Sprint version " + plugin.getDescription().getVersion());
				sender.sendMessage(ChatColor.DARK_AQUA + "For help, type " + ChatColor.DARK_PURPLE + "/antisprint help" + ChatColor.DARK_AQUA + ".");
				return true;
			}

			if (args.length == 1) {
				switch (args[0].toLowerCase()) {
					case "toggle":
						if (!checkPerms(sender)) return true;

						boolean enabled = plugin.getConfig().getBoolean("Enabled");
						plugin.getConfig().set("Enabled", !enabled);
						plugin.saveConfig();

						sender.sendMessage(ChatColor.GREEN + "[Anti-Sprint] Plugin successfully " + (enabled ? "disabled." : "enabled."));
						return true;

					case "help":
						sendHelpMessage(sender);
						return true;

					default:
						wrongCommandMessage(sender);
						return true;
				}
			}

			if (args.length == 2) {
				switch (args[0].toLowerCase()) {
					case "setfood":
						if (!checkPerms(sender)) return true;

						try {
							int food = Integer.parseInt(args[1]);
							if (food >= 0 && food <= 21) {
								plugin.getConfig().set("Minimum-Food", food);
								plugin.saveConfig();
								sender.sendMessage(ChatColor.GREEN + "[Anti-Sprint] Minimum food level set to " + ChatColor.YELLOW + food + ChatColor.GREEN + ".");
							} else {
								sender.sendMessage(ChatColor.RED + "[Anti-Sprint] You must enter an integer between 0 and 21!");
							}
						} catch (NumberFormatException e) {
							sender.sendMessage(ChatColor.RED + "[Anti-Sprint] You must enter a valid number!");
						}
						return true;

					case "disallow":
						if (!checkPerms(sender)) return true;

						List<String> disallowedPlayers = plugin.getConfig().getStringList("Allow-Sprint");
						if (disallowedPlayers.remove(args[1])) {
							plugin.getConfig().set("Allow-Sprint", disallowedPlayers);
							plugin.saveConfig();
							sender.sendMessage(ChatColor.GREEN + "[Anti-Sprint] Sprinting has been disabled for " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + ".");
						} else {
							sender.sendMessage(ChatColor.RED + "[Anti-Sprint] Sprinting was not enabled for " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + ".");
						}
						return true;

					case "allow":
						if (!checkPerms(sender)) return true;

						List<String> allowedPlayers = plugin.getConfig().getStringList("Allow-Sprint");
						if (!allowedPlayers.contains(args[1])) {
							allowedPlayers.add(args[1]);
							plugin.getConfig().set("Allow-Sprint", allowedPlayers);
							plugin.saveConfig();
							sender.sendMessage(ChatColor.GREEN + "[Anti-Sprint] Sprinting has been enabled for " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + ".");
						} else {
							sender.sendMessage(ChatColor.RED + "[Anti-Sprint] Sprinting was already enabled for " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + ".");
						}
						return true;

					default:
						wrongCommandMessage(sender);
						return true;
				}
			}

			wrongCommandMessage(sender);
			return true;
		}

		return false;
	}

	private boolean checkPerms(CommandSender sender) {
		if (sender.hasPermission("antisprint.configure") || sender instanceof ConsoleCommandSender) {
			return true;
		}
		sender.sendMessage(ChatColor.RED + "[Anti-Sprint] You do not have permission!");
		return false;
	}

	private void sendHelpMessage(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "[Anti-Sprint] List of help commands:");
		sender.sendMessage(ChatColor.DARK_AQUA + "-------------------------------------------");
		sender.sendMessage(ChatColor.YELLOW + "/antisprint - " + ChatColor.DARK_AQUA + "Shows basic plugin information.");
		sender.sendMessage(ChatColor.YELLOW + "/antisprint help - " + ChatColor.DARK_AQUA + "Shows list of commands.");
		sender.sendMessage(ChatColor.YELLOW + "/antisprint toggle - " + ChatColor.DARK_AQUA + "Enables/disables the plugin.");
		sender.sendMessage(ChatColor.YELLOW + "/antisprint setfood <food> - " + ChatColor.DARK_AQUA + "Sets minimum food level required to sprint. Set to 21 to disallow all sprinting.");
		sender.sendMessage(ChatColor.YELLOW + "/antisprint allow <player> - " + ChatColor.DARK_AQUA + "Enables the given player to sprint if they have permission.");
		sender.sendMessage(ChatColor.YELLOW + "/antisprint disallow <player> - " + ChatColor.DARK_AQUA + "Disables the given player from sprinting if they were previously allowed.");
	}

	private void wrongCommandMessage(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "[Anti-Sprint] Unknown command.");
		sender.sendMessage(ChatColor.RED + "Type " + ChatColor.DARK_PURPLE + "/antisprint help " + ChatColor.RED + "for a list of commands.");
	}
}
