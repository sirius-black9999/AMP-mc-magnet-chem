package com.aerialmage.magnetchem.fluids;

import com.aerialmage.magnetchem.core.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;

public class LiquidWorldgen extends Fluid {

	IIcon[] iconArray;
	public LiquidWorldgen(String fluidName) {
		super(fluidName);
		
	}
	@SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */

	@Mod.EventHandler
    public void registerIcons(TextureStitchEvent.Pre evt)
    {
        iconArray = new IIcon[2];
        iconArray[0] = evt.map.registerIcon(Reference.MOD_ID + ":liquid_worldgen_still");
        iconArray[1] = evt.map.registerIcon(Reference.MOD_ID + ":liquid_worldgen_flow");
    }
	@Mod.EventHandler
	public void finishIcons(TextureStitchEvent.Post evt)
	{
		setFlowingIcon(iconArray[1]);
        setStillIcon(iconArray[0]);
	}
}
