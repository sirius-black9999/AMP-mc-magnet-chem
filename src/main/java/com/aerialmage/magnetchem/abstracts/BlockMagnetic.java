package com.aerialmage.magnetchem.abstracts;

import com.aerialmage.magnetchem.entry.magnetchem;
import com.aerialmage.magnetchem.interfaces.IToolGaussMeter;
import com.aerialmage.magnetchem.tileentities.TileEntityMagnetic;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockMagnetic extends BlockContainer {

	protected BlockMagnetic(Material par2Material) {
		super(par2Material);
		// TODO Auto-generated constructor stub
	}
	public boolean blockActivated(EntityPlayer entityplayer) {
		Item equipped = entityplayer.getCurrentEquippedItem() != null ? entityplayer.getCurrentEquippedItem().getItem() : null;
		
		if (equipped instanceof IToolGaussMeter && ((IToolGaussMeter) equipped).canMeasure(entityplayer)) {
			((IToolGaussMeter) equipped).MeterUsed(entityplayer);
			return true;
		}
		return false;
	}
	protected void applyMagneticTransmission(World world, int x, int y, int z) {
		TileEntity te = null;
		switch(world.getBlockMetadata(x, y, z)%6)
		{
		case 0:
			te = world.getTileEntity(x, y-1, z);
			break;
		case 1:
			te = world.getTileEntity(x, y + 1, z);
			break;
		case 2:
			te = world.getTileEntity(x, y, z - 1);
			break;
		case 3:
			te = world.getTileEntity(x, y, z + 1);
			break;
		case 4:
			te = world.getTileEntity(x - 1, y, z);
			break;
		case 5:
			te = world.getTileEntity(x + 1, y, z);
			break;
		}
		//System.out.println("calling "+world.getBlockMetadata(x, y, z)%6+": "+TE);
		//System.out.println("from: "+world.getBlockTileEntity(x, y, z));
		if(te != null && te instanceof TileEntityMagnetic)
		{
			((TileEntityMagnetic) world.getTileEntity(x, y, z)).adjacentMagnets((TileEntityMagnetic)te);
		}
		else
		{
			((TileEntityMagnetic) world.getTileEntity(x, y, z)).dieOff();
		}
	}


	protected boolean BlockIsActiveCell(World world, int x, int y, int z) {
		if(world.getBlock(x, y, z).equals(magnetchem.blockRedCellV2))
			if(world.getBlockMetadata(x, y, z)%5 == 2 || world.getBlockMetadata(x, y, z)%5 == 3)
				return true;
		return false;
	}

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return null;
    }

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityMagnetic();
	}
}
