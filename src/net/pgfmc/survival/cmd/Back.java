package net.pgfmc.survival.cmd;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.core.DimManager;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.dim.SpawnProtection;

/**
 * Command to teleport the player to their last death location.
 * @author bk
 */
public class Back implements CommandExecutor, Listener {
	
	/**
	 * saves a player's last location.
	 * @param p Player
	 * @param loc The Player's last Location
	 */
	public static void logBackLocation(OfflinePlayer p, Location loc)
	{
		PlayerData.setData(p, "backLoc", loc);
	}
	
	/**
	 * Gets a player's last Location
	 * @param p Player
	 * @return A player's last Location, null if none
	 */
	public Location getBackLocation(OfflinePlayer p)
	{
		return Optional.ofNullable((Location) PlayerData.getData(p, "backLoc")).orElse(null);
	}
	
	/**
	 * Teleports a player their back location if it exists
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) { return true; }
		
		Player p = (Player) sender;
		
		if (!DimManager.isSurvivalWorld(p.getWorld()))
		{
			p.sendMessage("§cYou can only use this command in the Survival world.");
			return true;
		}
		
		Location dest = getBackLocation(p);
		if (dest == null)
		{
			p.sendMessage("§cYou do not have a back location.");
			return true;
		}
		
		p.sendMessage("§6You will be teleported in 5 seconds.");
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			
			@Override
			public void run()
			{
				logBackLocation(p, null);
				SpawnProtection.TEMP_PROTECT(p, 20 * 2);
				p.teleport(dest); // may be wrong I will test
				p.sendMessage("§aPoof!");
			}
			
		}, 20 * 5);
		
		
		return true;
	}
	
	/**
	 * Sets the back location on death
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		Player p = e.getEntity();
		if (!DimManager.isSurvivalWorld(p.getWorld())) { return; }
		
		logBackLocation(p, p.getLocation());
	}

}
