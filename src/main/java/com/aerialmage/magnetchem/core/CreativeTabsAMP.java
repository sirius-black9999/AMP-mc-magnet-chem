package com.aerialmage.magnetchem.core;

import com.aerialmage.magnetchem.entry.magnetchem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class CreativeTabsAMP extends CreativeTabs {

	public static CreativeTabsAMP instance = new CreativeTabsAMP("Aerial Mage Prototype");
	public CreativeTabsAMP(String label) {
		super(label);
	}

	@SideOnly(Side.CLIENT)

	@Override
    public ItemStack getIconItemStack() {
		return new ItemStack(magnetchem.blockRedCellV2, 0, 2);
	}

    @Override
    public Item getTabIconItem() {
        return null;
    }

    @Override
    public String getTranslatedTabLabel() {
		return "Aerial mage Prototype";
	}
}
