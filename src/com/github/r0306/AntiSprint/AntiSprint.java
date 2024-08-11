package com.github.r0306.AntiSprint;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiSprint extends JavaPlugin {

	private Executor cmd;

	@Override
	public void onEnable() {
		cmd = new Executor(this);
		getCommand("antisprint").setExecutor(cmd);
		getServer().getPluginManager().registerEvents(new SprintListener(this), this);
		loadConfig();
		getLogger().info("Anti-Sprint version [" + getDescription().getVersion() + "] loaded.");
	}

	@Override
	public void onDisable() {
		getLogger().info("Anti-Sprint version [" + getDescription().getVersion() + "] unloaded.");
	}

	public void loadConfig() {
		List<String> list = new ArrayList<>();
		FileConfiguration cfg = getConfig();
		FileConfigurationOptions cfgOptions = cfg.options();
		cfg.addDefault("Enabled", true);
		cfg.addDefault("Minimum-Food", 21);
		cfg.addDefault("Allow-Sprint", list);
		cfgOptions.copyDefaults(true);
		cfgOptions.header("This is the Anti-Sprint configuration file. Please use the in-game commands to set the options.");
		cfgOptions.copyHeader(true);
		saveConfig();
	}
}
