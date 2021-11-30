package net.pgfmc.survival.masterbook;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.pgfmc.core.dim.DimManager;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.roles.Permissions;
import net.pgfmc.core.roles.Roles;

/**
 * Gives the player a help book if it is their first time playing
 * @author bk
 *
 */
public class FirstJoin implements Listener {
	
	
	/*
	 * bruh
	 */
	public static ItemStack helpBook;
	
	@EventHandler
	public void onFirstJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		if (!p.hasPlayedBefore())
		{
			if (DimManager.isInWorld(p.getWorld().getName(), "hub"))
			{
				
				PlayerData pd = new PlayerData(e.getPlayer());
				pd.setData("AFK", false);
				Roles.recalculateRoles(pd);
				Permissions.recalcPerms(pd);
				
				
				if (helpBook == null) {
					helpBook = new ItemStack(Material.BOOK);
					
					ItemMeta imeta = helpBook.getItemMeta();
					
					imeta.setDisplayName("§r§a| Commands |");
					helpBook.setItemMeta(imeta);
				}
				
				
				p.getInventory().addItem(helpBook);
			}
		}
	}
	
	@EventHandler
	public void onOpenHelpBook(PlayerInteractEvent e)
	{
		
		if (helpBook == null) {
			helpBook = new ItemStack(Material.BOOK);
			
			ItemMeta imeta = helpBook.getItemMeta();
			
			imeta.setDisplayName("§r§a| Commands |");
			helpBook.setItemMeta(imeta);
		}
		
		if (e.getItem() != null && e.getItem().equals(helpBook))
		{
			e.getPlayer().performCommand("commands");
		}
	}

}
