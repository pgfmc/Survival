package net.pgfmc.survival.masterbook;


import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.playerdataAPI.PlayerData;

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
		
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (pd == null) {
			
			pd = new PlayerData(e.getPlayer());
			
			
			pd.setData("AFK", false);
			Roles.recalculateRoles(pd);
			Permissions.recalcPerms(pd);
			
			if (helpBook == null) {
				helpBook = new ItemStack(Material.BOOK);
				
				ItemMeta imeta = helpBook.getItemMeta();
				
				imeta.setDisplayName("�r�b| �lCommands �r�b|");
				helpBook.setItemMeta(imeta);
			}
			
			pd.sendMessage("hi");
			e.getPlayer().getInventory().addItem(helpBook.clone());
		}
		
	}
	
	@EventHandler
	public void onOpenHelpBook(PlayerInteractEvent e)
	{
		
		if (helpBook == null) {
			helpBook = new ItemStack(Material.BOOK);
			
			ItemMeta imeta = helpBook.getItemMeta();
			
			imeta.setDisplayName("�r�b| �lCommands �r�b|");
			helpBook.setItemMeta(imeta);
		}
		
		if (e.getItem() != null && e.getItem().getType() == Material.BOOK && e.getItem().getItemMeta().getDisplayName().equals("�r�b| �lCommands �r�b|"))
		{
			e.getPlayer().performCommand("commands");
		}
	}

}
