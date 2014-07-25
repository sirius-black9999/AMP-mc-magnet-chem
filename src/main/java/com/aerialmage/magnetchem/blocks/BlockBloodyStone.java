package com.aerialmage.magnetchem.blocks;

import com.aerialmage.magnetchem.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBloodyStone extends Block {


    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;
	public BlockBloodyStone(Material par2Material) {
		super(par2Material);
		setTickRandomly(true);
	}
	@Override
	public void updateTick(World par1World, int x, int y, int z,
			Random par5Random) {

    	int meta = par1World.getBlockMetadata(x, y, z);
    	if(meta+1 < 8)
    	{
    		par1World.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
    	}
    	else
    	{
    		par1World.setBlock(x, y, z, Blocks.redstone_block);
    	}
		super.updateTick(par1World, x, y, z, par5Random);
	}
	@Override
	public IIcon getIcon(int par1, int par2)
	{

		return iconArray[par2];
	};

    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IIconRegister par1IconRegister)
    {
        iconArray = new IIcon[8];
        for (int i = 0; i < this.iconArray.length; ++i)
        {
            iconArray[i] = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName() + "_" + i);
        }
    }
}
