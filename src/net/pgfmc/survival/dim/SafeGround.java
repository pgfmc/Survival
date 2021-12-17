package net.pgfmc.survival.dim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class SafeGround {
	
	public static List<Material> unsafeMats = new ArrayList<Material>(Arrays.asList(Material.AIR, Material.LAVA, Material.WATER, Material.CACTUS, Material.FIRE, Material.WITHER_ROSE));
	
	// pass in Location
	// get nearest safe location
	// 
	// /stuck will do above but also tp out of any claims/ownables
	
	public static Location toSafe(Location loc)
	{
		Block b = loc.getBlock();
		
		if (!unsafeMats.contains(b.getType()))
		{
			return loc;
		}
		
		ChunkSnapshot chunk = loc.getWorld().getChunkAt(loc).getChunkSnapshot();
		
		return new Thread() {
			Location safeLoc = loc;
			boolean active = true;
			public void run()
			{
				for (int x = 0; x < 16; x++)
				{
					if (!active) { break; }
					for (int y = 0; y < 16; y++)
					{
						if (!active) { break; }
						for (int z = 0; z < 16; z++)
						{
							if (unsafeMats.contains(chunk.getBlockData(x, y, z).getMaterial()))
							{
								continue;
							} else
							{
								safeLoc = new Location(loc.getWorld(), x, y, z);
								active = false;
								break;
							}
						}
					}
				}
			}
		}.safeLoc;
		
	}
	
	/**
	 * Tests if the Block at a Location is safe for a player
	 * 
	 * @param loc Location of player
	 * @return true if safe
	 */
	public static boolean isSafe(Location loc)
	{
		Block b = loc.getBlock();
		Material m = b.getType();
		
		if (unsafeMats.contains(m))
		{
			return false;
		}
		
		return true;
			
	}
	
	
	/**
	 * Unstuck command, teleports the player away from claims
	 * 
	 * @param loc Location of the player
	 * @return Location away from claim
	 */
	/*
	public static Location getUnstuckLocation(Location loc)
	{
		OwnableBlock claim = Claim.getClosestClaim(loc);
		Location unstuck = loc;
		Random r = new Random();
		
		if (claim.inRange(loc))
		{
			// The random is to give a higher change of /stuck working
			switch (r.nextInt(4)) {
			case 0: unstuck.add(Claim.getRadius(claim), 0.0, r.nextInt(10));
			case 1: unstuck.add(Claim.getRadius(claim) * -1, 0.0, r.nextInt(10));
			case 2: unstuck.add(r.nextInt(10) , 0.0, Claim.getRadius(claim));
			case 3: unstuck.add(r.nextInt(10) , 0.0, Claim.getRadius(claim) * -1);
			}
			
			// Last safety check
			return toSafe(unstuck);
		} else
		{
			return null;
		}
	}
	*/
	
}
