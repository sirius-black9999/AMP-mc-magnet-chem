package com.aerialmage.magnetchem.items;

import com.aerialmage.magnetchem.core.CreativeTabsAMP;
import com.aerialmage.magnetchem.interfaces.IToolGaussMeter;
import com.aerialmage.magnetchem.tileentities.TileEntityMagnetic;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemGaussMeter extends Item implements IToolGaussMeter {

	public ItemGaussMeter() {

		super();
		setCreativeTab(CreativeTabsAMP.instance);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote)
			return false;
		Block block = world.getBlock(x, y, z);
		if(block == null)
			return false;
		
		if (!player.isSneaking())
			return false;
		
		TileEntityMagnetic TE = (TileEntityMagnetic)world.getTileEntity(x, y, z);
		System.out.println("("+x+", "+y+", "+z+"):"+world.getBlockMetadata(x, y, z)+" is "+TE);
		float gauss = -1;
		if(TE != null)
			gauss =  TE.gauss;
		float maxGauss = -1;
		if(TE != null)
		{
			maxGauss =  TE.totalGauss;
			TE.totalGauss = 0;
		}
		if(gauss == -1)
			FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("this block is not magnetic"));
		else
			FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("this block contains "+gauss+"G, with a max of "+maxGauss+"G"));
		
		return true;
	}
	
	@Override
	public boolean canMeasure(EntityPlayer player) {
		return true;
	}

	@Override
	public void MeterUsed(EntityPlayer player) {
		player.swingItem();
	}

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return false;
    }

//    @Override
//	public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6) {
//		return true;
//	}
	

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		itemIcon = par1IconRegister.registerIcon(getUnlocalizedName().replace("item.", ""));
	}
}
