package net.pgfmc.survival.dim;

import java.util.EventListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.survival.Main;

/**
 * Attempts to protect the player in a undefendable state
 * eg. Traveling between worlds, joining the server, teleporting
 * @author bk
 *
 */
public class SpawnProtection implements EventListener {
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		TEMP_PROTECT(e.getPlayer(), 20 * 4);
	}
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent e)
	{
		TEMP_PROTECT(e.getPlayer(), 20 * 4);
	}
	
	public static void TEMP_PROTECT(Player p, int ticks)
	{
		p.setInvulnerable(true);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() { @Override public void run() {
			p.setInvulnerable(false);
			PlayerData.setData(p, "god", "false");
		}}, ticks);
	}

}
