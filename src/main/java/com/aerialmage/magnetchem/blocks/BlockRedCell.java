package com.aerialmage.magnetchem.blocks;

import com.aerialmage.magnetchem.core.CreativeTabsAMP;
import com.aerialmage.magnetchem.core.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.List;

public class BlockRedCell extends Block{

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;
	public BlockRedCell() {
		super(Material.circuits);
		setCreativeTab(CreativeTabsAMP.instance);
	}
	

    @SideOnly(Side.CLIENT)	

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public IIcon getIcon(int side, int metadata)
    {
        return this.iconArray[metadata % this.iconArray.length];
    }
    
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int metadata)
    {
        return 0;
    }


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 5));
		par3List.add(new ItemStack(this, 1, 10));
	}
    
    String[] iconNames = new String[]{"inactive", "pre_active", "active", "pre_cooldown", "cooldown"};
    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.iconArray = new IIcon[5];

        for (int i = 0; i < this.iconArray.length; ++i)
        {
            this.iconArray[i] = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName() + "_" + iconNames[i]);
        }
    }
    public int tickRate()
    {
    	return 1;
    }
    @Override
	    public void updateTick(World par1World, int x, int y, int z, java.util.Random par5Random)
	    {
    	
    	int meta = par1World.getBlockMetadata(x, y, z)%5;
    	if(meta < 4)
    	{
			par1World.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
			doBlockUpdate(par1World, x, y, z);
			par1World.scheduleBlockUpdateWithPriority(x, y, z, this, tickRate(), -1);
    	}
    	else
    	{
    		if(!par1World.isBlockIndirectlyGettingPowered(x, y, z))
    		{
    			par1World.setBlockMetadataWithNotify(x, y, z, 0, 2);
    			doBlockUpdate(par1World, x, y, z);
			}
			else
				par1World.scheduleBlockUpdateWithPriority(x, y, z, this, tickRate(), -1);
    	}
    };

  @Override
  public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) {return true;};
    
    @Override
    public void onNeighborBlockChange(World par1World, int x, int y, int z, Block otherID) {
    	if(par1World.getBlockMetadata(x, y, z)%5 == 0)
    	{
    		if(par1World.isBlockIndirectlyGettingPowered(x, y, z))
    		{	
    			par1World.scheduleBlockUpdateWithPriority(x, y, z, this, tickRate(), -1);
    		}
    		if(checkOtherBlocks(par1World, x,y,z,otherID)) 
    			if(otherIDIsCooldown(par1World, x,y,z,otherID))
    			{
	    			par1World.scheduleBlockUpdateWithPriority(x, y, z, this, tickRate(), -1);
    			}
    	}
    }
    private void revertBlocks(Chunk c)
    {
    	for(int x = 0; x < 16; x++)
			for(int y = 0; y < 256; y++)
				for(int z = 0; z < 16; z++)
					if(c.getBlock(x, y, z) == this)
					{

						c.setBlockMetadata(x, y, z, 0);
						
						//c.setBlockMetadataWithNotify(x, y, z, 0, 2);
						//doBlockUpdate(w, x, y, z);
						//w.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(), -1);
					}
    }
    @Mod.EventHandler
    public void onSave(ChunkEvent.Load evt)
    {
    	//System.out.println("calling onLoad");
    	Chunk c = evt.getChunk();
    	revertBlocks(c);
    }
//    @Mod.EventHandler
//    public void onSave(WorldEvent.Load evt)
//    {
//    	System.out.println("calling onLoad for "+evt.world.getChunkProvider().getLoadedChunkCount()+" chunks");
//    	for(Object pair : evt.world.activeChunkSet)
//    	{
//    		if(pair instanceof ChunkCoordIntPair)
//    		{
//    			ChunkCoordIntPair p = (ChunkCoordIntPair)pair;
//    			Chunk c = evt.world.getChunkFromChunkCoords(p.chunkXPos, p.chunkZPos);
//	    		System.out.println("unloading ("+c.xPosition+", "+c.zPosition+")");
//	    		revertBlocks(c);
//    		}
//    	}
//    }
@Override
public void onBlockAdded(World par1World, int x, int y, int z) {
	//par1World.scheduleBlockUpdateWithPriority(x, y, z, this.blockID, tickRate(), -1);
	super.onBlockAdded(par1World,x, y, z);
}

	private void doBlockUpdate(World par1World, int par2, int par3, int par4) {
	  par1World.notifyBlockOfNeighborChange(par2 + 1, par3, par4, this);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3, par4, this);
      par1World.notifyBlockOfNeighborChange(par2, par3, par4 + 1, this);
      par1World.notifyBlockOfNeighborChange(par2, par3, par4 - 1, this);
      par1World.notifyBlockOfNeighborChange(par2, par3 - 1, par4, this);
      par1World.notifyBlockOfNeighborChange(par2, par3 + 1, par4, this);
  	  par1World.notifyBlockOfNeighborChange(par2 + 1, par3+1, par4, this);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3+1, par4, this);
      par1World.notifyBlockOfNeighborChange(par2, par3+1, par4 + 1, this);
      par1World.notifyBlockOfNeighborChange(par2, par3+1, par4 - 1, this);
  	  par1World.notifyBlockOfNeighborChange(par2 + 1, par3-1, par4, this);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3-1, par4, this);
      par1World.notifyBlockOfNeighborChange(par2, par3-1, par4 + 1, this);
      par1World.notifyBlockOfNeighborChange(par2, par3-1, par4 - 1, this);
  	  par1World.notifyBlockOfNeighborChange(par2 + 1, par3, par4+1, this);
      par1World.notifyBlockOfNeighborChange(par2 + 1, par3, par4-1, this);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3, par4 + 1, this);
      par1World.notifyBlockOfNeighborChange(par2 - 1, par3, par4 - 1, this);
	}
	private boolean checkOtherBlocks(World World, int x, int y, int z, Block otherID) {
		for(int i = -1; i < 2; i++)
			for(int j = -1; j < 2; j++)
				for(int k = -1; k < 2; k++)
					if((i==0 || j == 0 || k == 0) && !(i==0 && j==0 && k==0))
						if(World.getBlock(x + i, y + j, z + k) == this)
							if(World.getBlockMetadata(x+i, y+j, z+k) == 2)
								return true;
		return false;
	}
	private boolean otherIDIsCooldown(World World, int x, int y, int z, Block otherID) {
		int active = 0;
		for(int i = -1; i < 2; i++)
			for(int j = -1; j < 2; j++)
				for(int k = -1; k < 2; k++)
					if((i==0 || j == 0 || k == 0) && !(i==0 && j==0 && k==0))
						if(World.getBlock(x + i, y + j, z + k) == this)
						{
							if(World.getBlockMetadata(x+i, y+j, z+k) == 2 || World.getBlockMetadata(x+i, y+j, z+k) == 1)
								active++;
							if(World.getBlockMetadata(x+i, y+j, z+k) == 3 )
								active = 10;
						}
		return active > 0 && active < 3;
	}
}
 