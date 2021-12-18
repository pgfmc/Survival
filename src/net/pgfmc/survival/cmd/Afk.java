package net.pgfmc.survival.cmd;

import java.util.Optional;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Command toggle AFK mode.
 * 
 * AFK mode blocks all incoming damage, and prevents mob aggro.
 * @author CrimsonDart
 */
public class Afk  implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		// adds or removes players depending on if they are Afk or not.
		
		if ((boolean) PlayerData.getData(p, "AFK")) {
			p.sendMessage("§cAFK mode off.");
			// If the player was in god mode when entering afk, keep in god mode
			if (!(boolean) Optional.ofNullable(PlayerData.getData(p, "god")).orElse(false))
			{
				p.setInvulnerable(false);
			}
			p.resetTitle();
			PlayerData.setData(p, "AFK", false);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 8);
		} else {
			p.sendMessage("§aAFK mode on!");
			p.sendTitle("", "§7Click or jump to exit §6§lAFK §r§7mode, or type §6§l/afk!", 0, 1000000000, 0);
			p.setInvulnerable(true);
			PlayerData.setData(p, "AFK", true);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
		}
		
		return true;
	}
	
	// a getter for external classes to get wether or not a player is AFK
	public static boolean isAfk(Player player) {
		
		return (boolean) Optional.ofNullable(
				PlayerData.getData(player, "AFK")
				).orElse(false);
	}
}