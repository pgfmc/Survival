package net.pgfmc.survival.cmd;

import java.util.Optional;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Command toggle AFK mode.
 * 
 * AFK mode blocks all incoming damage, and prevents mob aggro.
 * @author CrimsonDart
 */
public class Afk  implements CommandExecutor, Listener {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		toggleAfk((Player) sender);
		
		return true;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
		if (Afk.isAfk(p))
		{
			// Moving in the Y axis toggles AFK
			if (e.getTo().getY() != e.getFrom().getY())
			{
				toggleAfk(p);
				return;
			}
			
			// This lets them look, but not move
			e.setTo( // Set their "to" location to their "from" location
					e.getFrom()
					.setDirection( // Set their "to" direction to their "to" direction
							e.getTo().getDirection()));
			return;
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e)
	{
		if (Afk.isAfk(e.getPlayer()))
		{
			toggleAfk(e.getPlayer());
			return;
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) // disables AFK when a player leaves the server.
	{
		Player p = e.getPlayer();
		
		if (isAfk(p))
		{
			toggleAfk(p);
		}
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public static void toggleAfk(Player p)
	{
		if (isAfk(p)) // TURN AFK OFF
		{
			p.setInvulnerable(false);
			p.sendMessage("§cAFK mode off.");
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 8);
			
			PlayerData.setData(p, "AFK", false);
		} else // TURN AFK ON
		{
			// This deprecated method is nothing to worry about, getNoDamageTicks prevents AFK after 20 ticks of taking damage
			if (!p.isOnGround() || p.getNoDamageTicks() != 0 || p.isFlying()
					|| p.isDead() || p.isFrozen() || p.isGliding()
					|| p.isInWater() || p.isRiptiding() || p.isSleeping()
					|| p.isSwimming() || p.isClimbing())
			{
				p.sendMessage("§cYou cannot activate AFK right now.");
				return;
			}
			p.sendMessage("§aAFK mode on.");
			p.setInvulnerable(true);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			
			PlayerData.setData(p, "AFK", true);
		}
	}
	
	public static boolean isAfk(Player p)
	{
		return (boolean) Optional.ofNullable(PlayerData.getData(p, "AFK")).orElse(false);
	}
}