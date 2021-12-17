package net.pgfmc.survival;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.cmd.AfkEvents;
import net.pgfmc.survival.cmd.Back;
import net.pgfmc.survival.cmd.donator.Echest;
import net.pgfmc.survival.cmd.home.DelHome;
import net.pgfmc.survival.cmd.home.Home;
import net.pgfmc.survival.cmd.home.Homes;
import net.pgfmc.survival.cmd.home.SetHome;
import net.pgfmc.survival.cmd.tpa.Tpa;
import net.pgfmc.survival.cmd.tpa.Tpaccept;
import net.pgfmc.survival.cmd.tpa.Tpcancel;
import net.pgfmc.survival.cmd.tpa.Tpdeny;
import net.pgfmc.survival.dim.Worlds;
import net.pgfmc.survival.masterbook.FirstJoin;
import net.pgfmc.survival.masterbook.HelpCommand;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		Worlds.SURVIVAL.set(Bukkit.getWorld("survival"));
		Worlds.SURVIVAL_NETHER.set(Bukkit.getWorld("survival_nether"));
		Worlds.SURVIVAL_END.set(Bukkit.getWorld("survival_the_end"));
		
		getCommand("afk").setExecutor(new Afk());
		
		getCommand("tpa").setExecutor(new Tpa());
		getCommand("tpaccept").setExecutor(new Tpaccept());
		getCommand("tpdeny").setExecutor(new Tpdeny());
		getCommand("tpcancel").setExecutor(new Tpcancel());
		
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
		
		
	}
	
	@Override
	public void onDisable()
	{
		
	}

}
