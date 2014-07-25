package com.aerialmage.magnetchem.items;

import com.aerialmage.magnetchem.core.CreativeTabsAMP;
import com.aerialmage.magnetchem.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ItemRedCells extends Item {


    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
	private String iconName;
	public static final String[] bandedNames = new String[] {"glass", "redstone block"};
	private static String[] itemNames = { "banded_glass", "banded_redstone_block"};
	public ItemRedCells() {
		super();
		setCreativeTab(CreativeTabsAMP.instance);

		setHasSubtypes(true);
		setMaxDamage(0);
		
	}

	@Override
	public Item setUnlocalizedName(String par1Str) {
		iconName = par1Str;
		return super.setUnlocalizedName(par1Str);
	}
	
	@SuppressWarnings({ "all" })
	@Override
	public IIcon getIconFromDamage(int i) {
	    return i < icons.length ? icons[i] : null;
	}
	

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		int i = MathHelper.clamp_int(itemstack.getItemDamage(), 0, 15);
		return super.getUnlocalizedName()+"."+i;
	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList) {
//		for (int i = 0; i < 2; i++) {
//			itemList.add(new ItemStack(this, 1, i));
//		}
//	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
	    icons = new IIcon[itemNames.length];
	    int i = 0;
	    for (String csName : itemNames) {
	        icons[i++] = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + csName);
	    }
	}
}
