package net.pgfmc.survival.cmd.home;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.dim.DimManager;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.cmd.Back;
import net.pgfmc.survival.spawnprotection.SpawnProtection;

public class Home implements CommandExecutor {

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
			//TODO GUI
			return false;
		}
		
		HashMap<String, Location> homes = Homes.getHomes(p);
		String name = args[0].toLowerCase();
		if (homes.containsKey(name))
		{
			p.sendMessage("§aTeleporting to home §6" + name + "§a in 5 seconds!");
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() { @Override public void run() {
				SpawnProtection.TEMP_PROTECT(p, 20 * 2);
				Back.logBackLocation(p, p.getLocation());
				p.teleport(homes.get(name));
				p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
				}
			}, 20 * 5);
		} else
		{
			p.sendMessage("§aCould not find home §6" + name + "§a.");
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0);
		}
		
		return true;
	}

}
