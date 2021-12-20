package net.pgfmc.survival;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.cmd.AfkEvents;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("afk").setExecutor(new Afk());
		
		
		getServer().getPluginManager().registerEvents(new AfkEvents(), this);
		
	}
	
	@Override
	public void onDisable()
	{
		
	}

}
