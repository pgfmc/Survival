package net.pgfmc.survival.cmd.home;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.pgfmc.core.dim.DimManager;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Homes implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		Player p = (Player) sender;
		
		if (!DimManager.isSurvivalWorld(p.getWorld()))
		{
			p.sendMessage("§cYou can only use this command in the Survival world.");
			return true;
		}
		
		Set<String> homes = Homes.getHomes(p).keySet();
		String msg = "";
		
		if (homes.isEmpty())
		{
			p.sendMessage("§aHomes: §6No homes set");
			return true;
		}
		
		for (String name : homes)
		{
			msg += name + ", ";
		}
		msg = msg.substring(0, msg.length() - 2);
		
		p.sendMessage("§aHomes: §6" + msg);
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Location> getHomes(OfflinePlayer p)
	{
		return Optional.ofNullable((HashMap<String, Location>) PlayerData.getData(p, "homes")).orElse(new HashMap<String, Location>());
	}
	
	public void init()
	{
		/*
		 * is only ran once
		 * Will attempt to get homes of a player
		 * empty HashMap if no homes??????????????
		 */
		//PlayerData.getPlayerDataList().forEach(pd -> 
		
		PlayerData.set(x -> {
			
			Map<String, Location> map = new HashMap<>();
			FileConfiguration file = x.loadFile();
			
			if (file == null) {
				x.setData("homes", map);
				return;
			}
			
			ConfigurationSection confi = file.getConfigurationSection("homes");
			
			if (confi != null) {
				confi.getKeys(false).forEach(h -> {
					map.put(h, confi.getLocation(h));
				});
			}
			
			x.setData("homes", map);
		});
		
		
		/**
		pd.setData("homes", (HashMap<String, Location>) Optional.ofNullable(
				(pd.loadFromFile("homes")).get("homes"))
		).orElse(new HashMap<>()));*/
	}

}
