package com.aerialmage.magnetchem.proxy;

import com.aerialmage.magnetchem.containers.MagneticInductionContainer;
import com.aerialmage.magnetchem.containers.WorldgenLiquifierContainer;
import com.aerialmage.magnetchem.containers.WorldgenRegeneratorContainer;
import com.aerialmage.magnetchem.tileentities.TileEntityMagneticInductionFurnace;
import com.aerialmage.magnetchem.tileentities.TileEntityWorldgenLiquifier;
import com.aerialmage.magnetchem.tileentities.TileEntityWorldgenRegenerator;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class CommonProxy implements IGuiHandler{

    // Client stuff
    public void registerRenderers() {
            // Nothing here as the server doesn't render graphics or entities!
    }


    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z)
    {
        TileEntity te = world.getTileEntity(X, Y, Z);
        if (te != null && te instanceof TileEntityMagneticInductionFurnace)
        {
        	TileEntityMagneticInductionFurnace icte = (TileEntityMagneticInductionFurnace) te;
            return new MagneticInductionContainer(player.inventory, icte);
        }
        else if (te != null && te instanceof TileEntityWorldgenLiquifier)
        {
        	TileEntityWorldgenLiquifier icte = (TileEntityWorldgenLiquifier) te;
            return new WorldgenLiquifierContainer(player.inventory, icte);
        }
        else if (te != null && te instanceof TileEntityWorldgenRegenerator)
        {
        	TileEntityWorldgenRegenerator icte = (TileEntityWorldgenRegenerator) te;
            return new WorldgenRegeneratorContainer(player.inventory, icte);
        }
        else
        {
            return null;
        }
    }


    public World getClientWorld()
    {
        return null;
    }
}