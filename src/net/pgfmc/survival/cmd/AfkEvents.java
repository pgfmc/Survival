package net.pgfmc.survival.cmd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.core.dim.DimManager;
import net.pgfmc.core.playerdataAPI.PlayerData;
 /**
  * Provides the Effects of AFK mode.
  * 
  * Provides all the following effects:
  * 	Blocks incoming damage.
  * 	prevents all movement.
  * 	Disables all mob aggro.
  * 
  * to leave AFK mode, click or type /AFK.
  * 
  * @author CrimsonDart
  *
  */
public class AfkEvents implements Listener {
	
	@EventHandler
	public void preventMove(PlayerMoveEvent e) {
		if (!DimManager.isSurvivalWorld(e.getPlayer().getWorld())) { return; }
		
		if (Afk.isAfk(e.getPlayer())) {
			if (e.getTo().getY() != e.getFrom().getY()) // Turn off afk if they jump
			{
				e.getPlayer().performCommand("afk");
			} else
			{
				e.setTo(e.getFrom().setDirection(e.getTo().getDirection())); // Let the player move their camera, but not the player
			}
		}
	}
	
	@EventHandler
	public void click(PlayerInteractEvent e) {
		if (!DimManager.isSurvivalWorld(e.getPlayer().getWorld())) { return; }
		
		if (Afk.isAfk(e.getPlayer())) {
			e.getPlayer().performCommand("afk");
		}
	}
	
	@EventHandler
	public void onleave(PlayerQuitEvent e) { // disables AFK when a player leaves the server.
		
		PlayerData.setData(e.getPlayer(), "AFK", false);
		e.getPlayer().setInvulnerable(false);
		e.getPlayer().resetTitle();
	}

}
