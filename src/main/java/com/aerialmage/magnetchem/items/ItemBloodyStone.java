package com.aerialmage.magnetchem.items;

import com.aerialmage.magnetchem.core.CreativeTabsAMP;
import com.aerialmage.magnetchem.core.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class ItemBloodyStone  extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
	public static final String[] bandedNames = new String[] {"blood infused stone"};
	public ItemBloodyStone() {
		super();
		setCreativeTab(CreativeTabsAMP.instance);
		setHasSubtypes(true);
		setMaxDamage(64);
	}
	
	@SuppressWarnings({ "all" })
	@Override
	public IIcon getIconFromDamage(int i) {
		if(i == 64)
			return icons[0];
		i=(int) Math.ceil(i/8);
	    return i < icons.length ? icons[icons.length-i-1] : null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
	    icons = new IIcon[8];
	    for (int i = 0; i < this.icons.length; i++) {
	        icons[i] = par1IconRegister.registerIcon(Reference.MOD_ID + ":item_bloody_stone_" + i);
	    }
	}

}
