package net.pgfmc.survival.masterbook;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.pgfmc.core.DimManager;
import net.pgfmc.core.cmd.Blocked;
import net.pgfmc.core.inventoryAPI.InteractableInventory;
import net.pgfmc.core.inventoryAPI.PagedInventory;
import net.pgfmc.core.permissions.Roles.Role;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requestAPI.Request;
import net.pgfmc.core.requestAPI.Requester;
import net.pgfmc.core.requestAPI.Requester.Reason;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.cmd.home.Homes;
import net.pgfmc.teams.friends.Friends;
import net.pgfmc.teams.friends.Friends.Relation;

public class CommandsMenu {
	
	private static boolean TEAMINIT = false;
	
	public static class Homepage extends InteractableInventory {
		
		@SuppressWarnings("unchecked")
		public Homepage(PlayerData pd) {
			super(27, "Commands");
			if (!TEAMINIT) {
				TEAMINIT = (Bukkit.getServer().getPluginManager().getPlugin("Teams").isEnabled());
			}
			
			pd.getPlayer().getEffectivePermissions().forEach(x-> {
				/* 
				 * [] [] XX [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 */
				if (x.getPermission().equals("pgf.cmd.link") && x.getValue()) {
					
					if (pd.getData("Discord") != null) {
						createButton(Material.AMETHYST_SHARD, 2, "�dUnlink Discord", (p, e) -> {
							p.openInventory(new DiscordConfirm(pd).getInventory());
						});
					} else {
						createButton(Material.QUARTZ, 2, "�dLink Discord", (p, e) -> {
							p.closeInventory();
							p.performCommand("link");
						});
					}
					
				} else 
				
				/* 
				 * [] [] [] XX [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * toggles AFK in-menu
				 */
				if (x.getPermission().equals("pgf.cmd.afk") && x.getValue()) {
					
					if (Afk.isAfk(pd.getPlayer())) {
						createButton(Material.BLUE_ICE, 3, "�r�7AFK: �aEnabled", "�r�7Click to disable!", (p, e) -> {
							// p.closeInventory(); // Better if not close
							p.performCommand("afk");
							p.openInventory(new Homepage(pd).getInventory());
						});
					} else {
						createButton(Material.ICE, 3, "�r�7AFK: �cDisabled", "�r�7Click to enable!", (p, e) -> {
							// p.closeInventory(); // Better if not close
							p.performCommand("afk");
							p.openInventory(new Homepage(pd).getInventory());
						});
					}
				}
				
				/* 
				 * [] [] [] [] [] XX [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * Back command
				 */
				if (x.getPermission().equals("pgf.cmd.back") && x.getValue()) {
					createButton(Material.ARROW, 5, "�r�4Back", "�r�7Go back to your last location", (p, e) -> {
						p.openInventory(new BackConfirm(pd).getInventory());
					});
				}
				
				/* 
				 * [] [] [] [] [] [] XX [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * shows the current world's seed on screen.
				 */
				
				createButton(Material.BOOK, 6, "�r�dInfo", "�r�7Bring up the guidebook", (p, e) -> {
					p.closeInventory();
					p.openBook(Guidebook.getCopmleteBook());
				});
				
				
				/* 
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] XX [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * /dim command
				 */
				if (x.getPermission().equals("pgf.cmd.goto") && x.getValue()) {
					createButton(Material.SPYGLASS, 13, "�r�9Dimensions", "�r�7Go to other worlds!", (p, e) -> {
						p.openInventory(new DimSelect(pd).getInventory());
					});
				}
				
				/* 
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] XX [] [] [] [] [] []
				 * home menu
				 */
				if (x.getPermission().equals("pgf.cmd.home.*") && x.getValue()) {
					createButton(Material.COMPASS, 20, "�r�eHomes", (p, e) -> {
						p.openInventory(new HomeMenu(pd).getInventory());
					});
				}
				
				/* 
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] XX [] [] [] [] []
				 * home menu
				 */
				if (x.getPermission().equals("pgf.cmd.tp.tpa") && x.getValue()) {
					
					if (Bukkit.getOnlinePlayers().size() == 1) {
						createButton(Material.GRAY_CONCRETE, 21, "�r�5Tpa", "�r�cNo players online.", (p, e) -> {
							pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
						});
					} else {
						createButton(Material.ENDER_PEARL, 21, "�r�5Tpa", "�r�7Teleport to another player!", (p, e) -> {
							p.openInventory(new TpaList(pd).getInventory());
						});
					}
					
					
					
				}
				
				/* 
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] XX [] [] []
				 * home menu
				 */
				if (x.getPermission().equals("teams.friend.*") && x.getValue() && TEAMINIT) {
					createButton(Material.TOTEM_OF_UNDYING, 23, "�r�6Friends", (p, e) -> {
						p.openInventory(new FriendsList(pd).getInventory());
					});
				}
				
				/* 
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] XX [] []
				 * home menu
				 */
				if (x.getPermission().equals("bukkit.command.list") && x.getValue() && TEAMINIT) {
					createButton(Material.PLAYER_HEAD, 24, "�r�bPlayer List", (p, e) -> {
						 p.openInventory(new PlayerList(pd).getInventory());
					});
				}
				
				// Other buttons -
				
				/* 
				 * [] [] [] [] XX [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * home menu
				 */
				if (pd.getData("Roles") != null && ((List<Role>) pd.getData("Roles")).contains(Role.ADMIN)) {
					createButton(Material.EMERALD, 4, "�r�cAdmin", (p, e) -> {
						 // open up admin
					});
				}
				
				/* 
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] [] [] [] [] []
				 * [] [] [] [] XX [] [] [] []
				 * home menu
				 */
				if (x.getPermission().equals("pgf.cmd.donator.echest") && x.getValue()) {
					createButton(Material.ENDER_CHEST, 22, "�r�3Ender Chest", "�r�9VIP perk!", (p, e) -> {
						p.closeInventory();
						p.performCommand("echest");
					});
				}
				
				createButton(Material.LEVER, 9, "�r�4Requests", (p, e) -> {
					p.openInventory(new RequestList(pd).getInventory());
				});
			});
		}
	}
	
	// all nested inventories are below.
	// so ye, classes within classes
	// how about that, huh?
	
	/**
	 * Discord Link/unlink command inventory.
	 * @author CrimsonDart
	 *
	 */
	private static class DiscordConfirm extends InteractableInventory {
		
		public DiscordConfirm(PlayerData pd) {
			super(27, "�r�8Unlink Account?");
			
			/*
			 * checks if discord is already linked, and creates buttons corresponding to this information.
			 * sets buttons to the already-coded commands, because there is no difference. 
			 */
			
			createButton(Material.LIME_CONCRETE, 11, "�r�cUnlink", (p, e) -> {
				// p.closeInventory(); // I think it is better if the unlink doesn't close the inventory
				p.performCommand("unlink");
				p.openInventory(new Homepage(pd).getInventory());
			});
			
			// cancel button.
			createButton(Material.RED_CONCRETE, 15, "�r�7Cancel", (p, e) -> {
				p.openInventory(new Homepage(pd).getInventory());
			});
		}
	}
	
	private static class BackConfirm extends InteractableInventory {
		public BackConfirm(PlayerData pd) {
			super(27, "�r�8Tp to last location?");
			
			createButton(Material.LIME_CONCRETE, 11, "�r�dTeleport", (p, e) -> {
				p.closeInventory();
				p.performCommand("back");
			});
			
			createButton(Material.RED_CONCRETE, 15, "�r�7Cancel", (p, e) -> {
				p.closeInventory();
				p.openInventory(new Homepage(pd).getInventory());
			});
		}
	}
	
	private static class DimSelect extends PagedInventory<World> {
		
		public DimSelect(PlayerData pd) {
			super(SizeData.SMALL, "�r�5Dimension Select", DimManager.getAllWorlds(false), x-> {
				return createButton(Material.ENDER_PEARL, "�r�9" + x.getName(), null, (p, e) -> {
					p.performCommand("goto " + x.getName());
				});
			});
			setBackButton(new Homepage(pd));
		}
		
	}
	
	private static class HomeMenu extends InteractableInventory {
		
		public HomeMenu(PlayerData pd) {
			super(27, "�r�8Home");
			
			createButton(Material.FEATHER, 0, "�r�7Back", (p, e) -> {
				p.openInventory(new Homepage(pd).getInventory());
			});
			
			createButton(Material.ENDER_PEARL, 11, "�r�dGo to Home", (p, e) -> {
				p.openInventory(new HomeList(pd, "home ").getInventory());
			});
			
			
			if (!(Homes.getHomes(pd.getOfflinePlayer()).size() >= 3)) {
				createButton(Material.OAK_SAPLING, 13, "�r�aSet Home", (p, e) -> {
					p.openInventory(new SetConfirm(pd).getInventory());
				});
			}
			
			createButton(Material.FLINT_AND_STEEL, 15, "�r�cDelete Home", (p, e) -> {
				p.openInventory(new DelList(pd).getInventory());
			});
		}
		
		private static class HomeList extends PagedInventory<String> {
			
			public HomeList(PlayerData pd, String dingus) {
				super(SizeData.SMALL, "�r�8Home Select", Homes.getHomes(pd.getPlayer()).keySet(), x-> {
					return createButton(Material.PAPER, x, null, (p, e) -> {
						p.performCommand(dingus + x);
						p.closeInventory();
					});
				});
				setBackButton(new HomeMenu(pd));
			}
		}
		
		/**
		 * /sethome confirm inventory.
		 * 
		 * @author CrimsonDart
		 * {@link net.pgfmc.core.ChatEvents}
		 *
		 */
		private static class SetConfirm extends InteractableInventory {
			public SetConfirm(PlayerData pd) {
				super(27, "�r�8Set home here?");
				
				createButton(Material.LIME_CONCRETE, 11, "�r�aSet Home", (p, e) -> {
					pd.setData("tempHomeLocation", pd.getPlayer().getLocation());
					p.closeInventory();
					pd.sendMessage("�r�dType into chat to set the name of your Home!");
					pd.sendMessage("�r�dYou can only name the home for 4 minutes.");
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
						
						@Override
						public void run()
						{
							if (pd.getData("tempHomeLocation") != null) {
								pd.setData("tempHomeLocation", null);
								pd.sendMessage("�r�cYour home couldnt be set.");
							}
						}
						
					}, 20 * 60 * 4);
				});
				
				createButton(Material.RED_CONCRETE, 15, "�r�7Cancel", (p, e) -> {
					p.openInventory(new HomeMenu(pd).getInventory());
				});
			}
		}
		
		
		private static class DelList extends PagedInventory<String> {
			
			public DelList(PlayerData pd) {
				super(SizeData.SMALL, "�r�8Delete Home", Homes.getHomes(pd.getPlayer()).keySet(), x-> {
					return createButton(Material.PAPER, "�r�a" + x, null, (p, e) -> {
						p.performCommand("delhome " + x);
						// p.closeInventory(); // Better if not close
						p.openInventory(new DelList(pd).getInventory());
					});
				});
				setBackButton(new HomeMenu(pd));
			}
		}
		
	}
	
	private static class TpaList extends PagedInventory<Player> {
		public TpaList(PlayerData pd) {
			super(SizeData.SMALL, "�r�8Select who to teleport to!", Bukkit.getOnlinePlayers().stream().filter(x-> {
				return (!x.getUniqueId().toString().equals(pd.getUniqueId().toString()));
			}).collect(Collectors.toList()), (x) -> {
				return createButton(Material.PLAYER_HEAD, "�r�a" + x.getName(), null, (p, e) -> {
					p.performCommand("tpa " + x.getName());
					p.openInventory(new Homepage(pd).getInventory());
				});
			});
			
			setBackButton(new Homepage(pd));
		}
	}
	
	public static class FriendsList extends PagedInventory<PlayerData> {
		
		public FriendsList(PlayerData player) {
			super(SizeData.SMALL, "�r�8Friends List", Friends.getFriendsMap(player).keySet(), (x) -> {
				return createButton(Material.PAPER, "�r" + x.getRankedName(), null, (p, e) -> {
					
					p.openInventory(new FriendOptions(player, x).getInventory());
					
				});
			});
			setBackButton(new Homepage(player));
		}
		
		public static class FriendOptions extends InteractableInventory {

			public FriendOptions(PlayerData player, PlayerData friend) {
				super(27, "�r�8Options for " + friend.getRankedName());
				
				createButton(Material.ARROW, 12, "�r�cUnfriend", (p, e) -> {
					Friends.setRelation(player, Relation.NONE, friend, Relation.NONE);
					player.sendMessage("�cYou have Unfriended " + friend.getName() + ".");
					player.playSound(Sound.BLOCK_CALCITE_HIT);
					// player.getPlayer().closeInventory(); // Better if not close
					p.openInventory(new FriendOptions(player, friend).getInventory());
					
				});
				
				Relation r = Friends.getRelation(player, friend);
				
				if (r == Relation.FRIEND) {
					createButton(Material.NETHER_STAR, 14, "�r�eFavorite", (p, e) -> {
						
						Friends.setRelation(player, friend, Relation.FAVORITE);
						player.sendMessage("�r�6" + friend.getName() + " is now a favorite!");
						player.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
						// player.getPlayer().closeInventory(); // Better if not close
						p.openInventory(new FriendOptions(player, friend).getInventory());
						
					});
				} else if (r == Relation.FAVORITE) {
					createButton(Material.NETHER_STAR, 14, "�r�6Unfavorite", (p, e) -> {
						
						Friends.setRelation(player, friend, Relation.FRIEND);
						player.sendMessage("�r�c" + friend.getName() + " has Been unfavorited!");
						player.playSound(Sound.BLOCK_CALCITE_HIT);
						// player.getPlayer().closeInventory(); // Better if not close
						p.openInventory(new FriendOptions(player, friend).getInventory());
						
					});
				}
			}
		}
	}
	
	public static class PlayerList extends PagedInventory<PlayerData> {
		
		public PlayerList(PlayerData pd) {
			super(SizeData.SMALL, "�r�8Player List", PlayerData.stream()
					.filter(x-> {
						return (x != pd);
					})
					.collect(Collectors.toList()), x-> {
				return createButton(Material.PLAYER_HEAD, "�r�a" + x.getName(), (x.getOfflinePlayer().isOnline()) ? "�r�aOnline" : "�r�cOffline", (p, e) -> {
					p.openInventory(new PlayerOptions(pd, x).getInventory());
				});
			});
			
			setBackButton(new Homepage(pd));
		}
		
		private static class PlayerOptions extends InteractableInventory {
			
			public PlayerOptions(PlayerData pd, PlayerData player) {
				super(27, player.getRankedName());
				
				createButton(Material.FEATHER, 0, "�r�7Back", (p, e) -> {
					p.openInventory(new PlayerList(pd).getInventory());
				});
				
				pd.getPlayer().getEffectivePermissions().forEach(x-> {
					if (x.getPermission().equals("teams.friend.*") && x.getValue() && TEAMINIT) {
						
						Relation r = Friends.getRelation(pd, player);
						if (r == Relation.FRIEND || r == Relation.FAVORITE) {
							createButton(Material.TOTEM_OF_UNDYING, 11, "�r�cUnfriend", (p, e) -> {
								p.openInventory(new UnfriendConfirm(pd, player).getInventory());
							});
							
							if (r == Relation.FAVORITE) {
								createButton(Material.TOTEM_OF_UNDYING, 12, "�r�cUnfavorite", (p, e) -> {
									p.performCommand("unfav " + player.getName());
									// p.closeInventory();
									p.openInventory(new PlayerOptions(pd, player).getInventory());
								});
							} else {
								createButton(Material.TOTEM_OF_UNDYING, 12, "�r�eFavorite", (p, e) -> {
									p.performCommand("fav " + player.getName());
									// p.closeInventory();
									p.openInventory(new PlayerOptions(pd, player).getInventory());
								});
							}
							
						} else {
							createButton(Material.TOTEM_OF_UNDYING, 11, "�r�6Friend", (p, e) -> {
								p.openInventory(new FriendConfirm(pd, player).getInventory());
							});
						}
					}
					
					if (x.getPermission().equals("pgf.cmd.block") && x.getValue()) {
						
						if (Blocked.GET_BLOCKED(pd.getOfflinePlayer()).contains(player.getUniqueId())) {
							createButton(Material.RED_STAINED_GLASS_PANE, 14, "�r�4Unblock", (p, e) -> {
								p.performCommand("unblock " + player.getName());
								// p.closeInventory();
								p.openInventory(new PlayerOptions(pd, player).getInventory());
							});
						} else {
							createButton(Material.WHITE_STAINED_GLASS_PANE, 14, "�r�4Block", (p, e) -> {
								p.performCommand("block " + player.getName());
								// p.closeInventory();
								p.openInventory(new PlayerOptions(pd, player).getInventory());
							});
						}
					}
					
					createButton(Material.RED_BANNER, 15, "�r�4Report", "�r�7If someone is bullying or\ngriefing you, use this!" + "\nWIP");
					
					
				});
				
				
			}
			
			private static class FriendConfirm extends InteractableInventory {
				
				public FriendConfirm(PlayerData pd, PlayerData player) {
					super(27, "�r�6Friend " + player.getName() + "?");
					
					createButton(Material.LIME_CONCRETE, 11, "�r�aSend Request", (p, e) -> {
						// p.closeInventory();
						p.performCommand("friendrequest " + player.getName());
						p.openInventory(new PlayerOptions(pd, player).getInventory());
					});
					
					createButton(Material.RED_CONCRETE, 15, "�r�7Cancel", (p, e) -> {
						p.openInventory(new PlayerOptions(pd, player).getInventory());
					});
					
					
					
				}
			}
			
			private static class UnfriendConfirm extends InteractableInventory {
				
				public UnfriendConfirm(PlayerData pd, PlayerData player) {
					super(27, "�r�cUnfriend " + player.getName() + "?");
					
					createButton(Material.LIME_CONCRETE, 11, "�r�cUnfriend", (p, e) -> {
						// p.closeInventory();
						p.performCommand("unfriend " + player.getName());
						p.openInventory(new PlayerOptions(pd, player).getInventory());
					});
					
					createButton(Material.RED_CONCRETE, 15, "�r�7Cancel", (p, e) -> {
						p.openInventory(new PlayerOptions(pd, player).getInventory());
					});
					
					
					
				}
			}
		}
	}
	
	public static class RequestList extends PagedInventory<Request> {
		public RequestList(PlayerData pd) {
			super(SizeData.SMALL, "Pending Requests", Requester.ALLREQUESTS.stream()
					.filter(x-> {
						return (x.getTarget() == pd);
					})
					
					.collect(Collectors.toList())
					, x -> {
				return createButton(Material.ARROW, x.getParent().getName(), null, (p, e) -> {
					if (x.expireNow(Reason.Accept) != false) {
						x.act();
					} else {
						pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						p.openInventory(new RequestList(pd).getInventory());
					}
				});
			});
			setBackButton(new Homepage(pd));
		}
	}
}
