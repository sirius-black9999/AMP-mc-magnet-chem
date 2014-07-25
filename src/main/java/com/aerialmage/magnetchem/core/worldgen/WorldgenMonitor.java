package com.aerialmage.magnetchem.core.worldgen;

import com.aerialmage.magnetchem.entry.magnetchem;
import cpw.mods.fml.common.Mod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class WorldgenMonitor {
	
	HashMap<Byte, HashMap<Block, Integer>> freqs;
	public static HashMap<Byte, HashMap<Block, Integer>> multipliers;
	HashMap<Byte, Integer> total = new HashMap<Byte, Integer>();
	public static int highestMultiplier = 0;
	ArrayList<String> blockBlacklist = new ArrayList<String>();
	ArrayList<String> nameBlacklist = new ArrayList<String>();
	boolean finished = false;
		float maxChance = 0;
    Block maxChanceID = null;
	public WorldgenMonitor()
	{
//		 for(int i : new int[]{0,1,3, 4,5, 7, 8, 9, 10,11, 18, 52})
//		 {
//			 blockBlacklist.add(i);
//		 }
//		 for(String i : new String[]{"tile.auraNode"})
//		 {
//			 nameBlacklist.add(i);
//		 }
	}
	@Mod.EventHandler
	public void postGenerate(PopulateChunkEvent.Post evt)
	{
		if(evt.world.getWorldInfo().getTerrainType() != WorldType.FLAT)
		{
			Chunk c = evt.chunkProvider.provideChunk(evt.chunkX, evt.chunkZ);
			byte biome = c.getBiomeArray()[0];
			if(freqs == null)
				freqs = new HashMap<Byte, HashMap<Block,Integer>>();
			
			if(!freqs.containsKey(biome))
			{
				freqs.put(biome, new HashMap<Block, Integer>());
			}
			finished = true;
			if(!total.containsKey(biome) || total.get(biome) < 100000000)
			{
				recalcMultipliers(biome);
				
				for(int x = 0; x < 16; x++)
					for(int y = 0; y < 256; y++)
						for(int z = 0; z < 16; z++)
						{
							Block blockID = c.getBlock(x, y, z);
							//System.out.println(blockID);
							if(!blockBlacklist.contains(blockID) && !nameBlacklist.contains(Block.blockRegistry.getNameForObject(blockID)))
							{
								HashMap<Block, Integer> b = freqs.get(biome);
								if(b.containsKey(blockID))
									b.put(blockID, freqs.get(biome).get(blockID)+1);
								else
									b.put(blockID, 1);
								
								freqs.put(biome, b);
								if(total.containsKey(biome))
								{
									total.put(biome, total.get(biome)+1);
								}
								else
								{
									total.put(biome, 1);
								}
							}
						}
			}
		}
		//System.out.println(freqs.get(biome).toString());
	}
	public static Integer[] getAllUniqueBlockIds()
	{
		HashSet<Block> blockIDs = new HashSet<Block>();
		for(Entry<Byte, HashMap<Block, Integer>> biome : multipliers.entrySet())
		{
			for(Entry<Block, Integer> block : biome.getValue().entrySet())
			{
				blockIDs.add(block.getKey());
			}
		}
		return blockIDs.toArray(new Integer[blockIDs.size()]);
	}
	private void recalcMultipliers(byte biome)
	{
		maxChance = 0;
		if(multipliers == null)
			multipliers = new HashMap<Byte, HashMap<Block,Integer>>();

		if(!multipliers.containsKey(biome))
		{
			multipliers.put(biome, new HashMap<Block, Integer>());
		}
		highestMultiplier = 0;
		for(Entry<Block, Integer> e : freqs.get(biome).entrySet())
		{
			int localTotal = 0;
			if(total.containsKey(biome))
			 localTotal = total.get(biome);
			if((float)e.getValue()/(float)localTotal > maxChance)
			{
				maxChance = (float)e.getValue()/(float)localTotal;
				maxChanceID = e.getKey();
			}
			if((int) (maxChance/(((float)e.getValue()/(float)localTotal))) > highestMultiplier)
				highestMultiplier = (int) (maxChance/(((float)e.getValue()/(float)localTotal)));
			HashMap<Block, Integer> b = multipliers.get(biome);
			b.put(e.getKey(), (int) (maxChance/(((float)e.getValue()/(float)localTotal))));
			multipliers.put(biome, b);
			//System.out.println(Block.blocksList[(Integer) e.getKey()].getLocalizedName()+":\twas generated with a  "+(float)e.getValue()/(float)total*100+"% chance in chunk ("+evt.chunkX+", "+evt.chunkZ+") and is a value of "+maxChance/(((float)e.getValue()/(float)total))+" times "+Block.blocksList[maxChanceID].getLocalizedName());
		}
	}
	@Mod.EventHandler
	public void OnWorldLoad(WorldEvent.Load evt)
	{
		System.out.println("loading worldgen data collection");
		BufferedReader cfg = null;
		try
		{
			cfg = new BufferedReader(new FileReader(magnetchem.configAlt));
			load(cfg);
			cfg.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("file not found during loading, bailing out");
			System.out.println(e.toString());
			return;
		}
		catch(Exception e)
		{
			System.err.println("Aerial mage prototype has a problem loading its configuration file:");
    		e.printStackTrace(System.err);
		}
	}
	@Mod.EventHandler
	public void OnWorldSave(WorldEvent.Save evt)
	{
		System.out.println("saving worldgen data collection");
		BufferedWriter cfg = null;
		try
		{
			magnetchem.configAlt.delete();
			cfg = new BufferedWriter(new FileWriter(magnetchem.configAlt, false));
			save(cfg);

			cfg.flush();
			cfg.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("file not found");
			System.out.println(e.toString());
		}
		catch(Exception e)
		{
			System.err.println("Aerial mage prototype has a problem loading its configuration file:");
    		e.printStackTrace(System.err);
		}
	}
	

	private void save(BufferedWriter cfg) throws IOException 
	{
		if(freqs == null)
		{
			System.out.println("frequency table is empty, this should not happen, bailing out");
			return;
		}
		for(Entry<Byte, Integer> t : total.entrySet())
		{
			cfg.write(t.getKey()+"\t"+t.getValue());
			cfg.newLine();
		}
		for(Entry<Byte, HashMap<Block,Integer>> biome : freqs.entrySet())
		{
			byte b = biome.getKey();
			for(Entry<Block, Integer> frequency : biome.getValue().entrySet())
			{
				String biomeStr = BiomeGenBase.getBiome(b).biomeName;
				if(biomeStr.length() < 7)
					biomeStr = biomeStr+"\t";
				if(biomeStr.length() < 15)
					biomeStr = biomeStr+"\t";
				cfg.write(b+"\t"+frequency.getKey()+"\t"+frequency.getValue()+"\t"+biomeStr+"\t"+Block.getIdFromBlock(frequency.getKey()));
				cfg.newLine();
			}
		}
	}
	private void load(BufferedReader cfg) throws NumberFormatException, IOException {
		freqs = new HashMap<Byte, HashMap<Block,Integer>>();
		while(cfg.ready())
		{
			String[] lastLine =  cfg.readLine().split("\t");
			if(lastLine.length == 2)
			{
				total.put(Byte.parseByte(lastLine[0]), Integer.parseInt(lastLine[1]));
			}
			else
			{
				//System.out.println(lastLine.toString());
				byte biome = Byte.parseByte(lastLine[0]);
				Block blockID = Block.getBlockById(Integer.parseInt(lastLine[1]));
				int freq = Integer.parseInt(lastLine[2]);
	//			for(int i = 0; i < Block.blocksList.length; i++)
	//			{
	//				if(Block.blocksList[i].getUnlocalizedName().equals(lastLine[1]))
	//					{
	//					blockID = i;
	//					break;
	//					}
	//					
	//			}
				
					
				if(!freqs.containsKey(biome))
				{
					freqs.put(biome, new HashMap<Block, Integer>());
				}
				HashMap<Block, Integer> b = freqs.get(biome);
				b.put(blockID, freq);
				freqs.put(biome, b);
			}
		}
		for(Entry<Byte, HashMap<Block,Integer>> biome : freqs.entrySet())
		{
			byte b = biome.getKey();
			recalcMultipliers(b);
		}
	}
	@SuppressWarnings("unchecked")
	public static int getItemRarity(ItemStack target, World world, int xCoord, int zCoord) {
		if(target != null)
		{
			if(WorldgenMonitor.multipliers.containsKey(world.getChunkFromBlockCoords(xCoord, zCoord).getBiomeArray()[0]))
			{
				if(WorldgenMonitor.multipliers.get(world.getChunkFromBlockCoords(xCoord, zCoord).getBiomeArray()[0]).containsKey(target))
				{
					return WorldgenMonitor.multipliers.get(world.getChunkFromBlockCoords(xCoord, zCoord).getBiomeArray()[0]).get(target)*10;
				}
			}
			else
			{
				for(Entry<Byte, HashMap<Block, Integer>> entry : WorldgenMonitor.multipliers.entrySet())
				{
					byte b = entry.getKey();
					//System.out.println(b+", "+entry.getValue()+", "+target);
					if(entry.getValue().containsKey(target))
					{
						return entry.getValue().get(target)*10;
					}
				}
	//			if(((HashMap<Integer, Integer>)WorldgenMonitor.multipliers.entrySet().toArray()[0]).containsKey(target.itemID))
	//			{
	//				return ((HashMap<Integer, Integer>)WorldgenMonitor.multipliers.entrySet().toArray()[0]).get(target.itemID);
	//			}
			}
		}
		return -1;
	}
}
