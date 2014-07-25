package com.aerialmage.magnetchem.blocks;

import com.aerialmage.magnetchem.core.Reference;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockMulticolorWool extends Block {

	
	private static IIcon topIconA;
	private static IIcon sideIconA;
	private static IIcon bottomIconA;
	
	public BlockMulticolorWool(Material par2Material) {
		super(par2Material);
		
	}



	@SideOnly(Side.CLIENT)
    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public IIcon getIcon(int par1, int par2)
    {
			return par1 == 1 ? BlockMulticolorWool.topIconA : (par1 == 0 ? BlockMulticolorWool.bottomIconA : BlockMulticolorWool.sideIconA);
    }
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
    	//System.out.println(mod_AMP.modid + ":" + (this.getUnlocalizedName().substring(5)));
        BlockMulticolorWool.bottomIconA = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName()+"_bottom");
        BlockMulticolorWool.sideIconA = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName()+"_side");
        BlockMulticolorWool.topIconA = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getTextureName()+"_top");
    }
}
