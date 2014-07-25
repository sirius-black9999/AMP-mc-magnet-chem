package com.aerialmage.magnetchem.proxy;

import com.aerialmage.magnetchem.GUI.MagFurnaceGui;
import com.aerialmage.magnetchem.GUI.WorldgenLiquifierGui;
import com.aerialmage.magnetchem.GUI.WorldgenRegeneratorGui;
import com.aerialmage.magnetchem.tileentities.TileEntityMagneticInductionFurnace;
import com.aerialmage.magnetchem.tileentities.TileEntityWorldgenLiquifier;
import com.aerialmage.magnetchem.tileentities.TileEntityWorldgenRegenerator;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClientProxy extends com.aerialmage.magnetchem.proxy.CommonProxy {
        
        @Override
        public void registerRenderers() {
                // This is for rendering entities and so forth later on
        }
        

        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
        {
        	TileEntity te = null;
        		te = world.getTileEntity(x, y, z);
            if (te != null && te instanceof TileEntityMagneticInductionFurnace)
            {
            	System.out.println("opening magnetic induction furnace");
                return MagFurnaceGui.buildGUI(player.inventory, (TileEntityMagneticInductionFurnace) te);
            }
            else if(te != null && te instanceof TileEntityWorldgenLiquifier)
            {
            	System.out.println("opening worldgen liquifier");
            	return WorldgenLiquifierGui.buildGUI(player.inventory, (TileEntityWorldgenLiquifier) te);
            }
            else if(te != null && te instanceof TileEntityWorldgenRegenerator)
            {
            	System.out.println("opening worldgen regenerator");
            	return WorldgenRegeneratorGui.buildGUI(player.inventory, (TileEntityWorldgenRegenerator) te);
            }
            else
            {
                return null;
            }
        }

        @Override
        public World getClientWorld()
        {
            return FMLClientHandler.instance().getClient().theWorld;
        }
}