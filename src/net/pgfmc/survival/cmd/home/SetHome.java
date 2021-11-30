package net.pgfmc.survival.cmd.home;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.dim.DimManager;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class SetHome implements CommandExecutor, Listener {

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
		
		if (args.length == 0)
		{
			return false;
		}
		
		HashMap<String, Location> homes = Homes.getHomes(p);
		String name = args[0].toLowerCase();
		
		if (homes.containsKey(name))
		{
			p.sendMessage("§cYou cannot have duplicate home names.");
			return true;
		}
		
		if (homes.size() >= 3)
		{
			p.sendMessage("§cYou can only have 3 homes.");
			return true;
		}
		
		homes.put(name, p.getLocation());
		
		PlayerData.setData(p, "homes", homes).queue();
		p.sendMessage("§aSet home §6" + name + "§a!");
		
		
		return true;
	}
	
	public static boolean setHome(Player p, String name, Location loc)
	{
		HashMap<String, Location> homes = Homes.getHomes(p);
		
		if (homes.containsKey(name))
		{
			p.sendMessage("§cYou cannot have duplicate home names.");
			return false;
		}
		
		if (homes.size() >= 3)
		{
			p.sendMessage("§cYou can only have up to 3 homes.");
			return false;
		}
		
		if (loc != null)
		{
			homes.put(name, loc);
		} else
		{
			homes.put(name, p.getLocation());
		}
		
		p.sendMessage("§aSet home §6" + name + "§a!");
		PlayerData.setData(p, "homes", homes).queue();
		
		return true;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (pd.getData("tempHomeLocation") != null) {
			
			e.setCancelled(true);
			pd.setData("tempHomeLocation", null);
			SetHome.setHome(pd.getPlayer(), e.getMessage(), pd.getData("tempHomeLocation"));
			
			return;
		}
	}

}
