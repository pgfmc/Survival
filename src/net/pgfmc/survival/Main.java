package net.pgfmc.survival;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.cmd.AfkEvents;
import net.pgfmc.survival.cmd.Back;
import net.pgfmc.survival.cmd.Echest;
import net.pgfmc.survival.cmd.home.DelHome;
import net.pgfmc.survival.cmd.home.Home;
import net.pgfmc.survival.cmd.home.Homes;
import net.pgfmc.survival.cmd.home.SetHome;
import net.pgfmc.survival.cmd.tp.Tpa;
import net.pgfmc.survival.cmd.tp.Tpaccept;
import net.pgfmc.survival.cmd.tp.Tpdeny;
import net.pgfmc.survival.masterbook.FirstJoin;
import net.pgfmc.survival.masterbook.HelpCommand;
import net.pgfmc.survival.spawnprotection.ItemProtect;
import net.pgfmc.survival.spawnprotection.SpawnProtection;
public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		getCommand("afk").setExecutor(new Afk());
		
		getCommand("tpa").setExecutor(new Tpa());
		getCommand("tpaccept").setExecutor(new Tpaccept());
		getCommand("tpdeny").setExecutor(new Tpdeny());
		
		getCommand("home").setExecutor(new Home());
		getCommand("sethome").setExecutor(new SetHome());
		getCommand("delhome").setExecutor(new DelHome());
		getCommand("homes").setExecutor(new Homes());
		
		getCommand("echest").setExecutor(new Echest());
		
		getCommand("back").setExecutor(new Back());
		
		getCommand("commands").setExecutor(new HelpCommand());
		
		
		
		
		getServer().getPluginManager().registerEvents(new Back(), this);
		
		getServer().getPluginManager().registerEvents(new AfkEvents(), this);
		
		getServer().getPluginManager().registerEvents(new FirstJoin(), this);
		
		getServer().getPluginManager().registerEvents(new ItemProtect(), this);
		
		getServer().getPluginManager().registerEvents(new SpawnProtection(), this);
		
		getServer().getPluginManager().registerEvents(new SetHome(), this);	
		
		
		
		
		new Homes().init(); // Loads all homes into memory
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	

}
