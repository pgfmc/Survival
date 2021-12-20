package net.pgfmc.survival;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.cmd.AfkEvents;
import net.pgfmc.survival.cmd.donator.Echest;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("afk").setExecutor(new Afk());
		getCommand("echest").setExecutor(new Echest());
		
		
		getServer().getPluginManager().registerEvents(new AfkEvents(), this);
		
	}
	
	@Override
	public void onDisable()
	{
		
	}

}
