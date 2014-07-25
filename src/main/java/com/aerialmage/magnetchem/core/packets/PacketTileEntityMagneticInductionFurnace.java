package com.aerialmage.magnetchem.core.packets;

import com.aerialmage.magnetchem.core.BasePacket;
import com.aerialmage.magnetchem.entry.magnetchem;
import com.aerialmage.magnetchem.tileentities.TileEntityMagneticInductionFurnace;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketTileEntityMagneticInductionFurnace extends BasePacket {

	public PacketTileEntityMagneticInductionFurnace() {
	}
	@Override
	public boolean appliesTo(Object ent) {
			if(ent.getClass() == TileEntityMagneticInductionFurnace.class)
				return true;
				return false;
	}
	@Override
	public byte[] convertData(Object ent) {
		TileEntityMagneticInductionFurnace entity = (TileEntityMagneticInductionFurnace)ent;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = entity.xCoord;
		int y = entity.yCoord;
		int z = entity.zCoord;
		float gauss = entity.gauss;
		int[] items = entity.buildIntDataList();
		try
		{
			dos.writeByte(ID);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeFloat(gauss);
			dos.writeByte(items.length);
			if (items.length > 0)
			{
				for (int i = 0; i < items.length; i++)
				{
					dos.writeInt(items[i]);
				}
			}
			dos.flush();
			//System.out.println("sending packet at "+gauss+" / "+Arrays.toString(items));	
		}
		catch (IOException e)
		{
          // UNPOSSIBLE?
		}
		return bos.toByteArray();
	}
	@Override
	public void parseData(byte[] packetData) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(packetData);
		dat.readByte();
        int x = dat.readInt();
        int y = dat.readInt();
        int z = dat.readInt();
        float gauss = dat.readFloat();

        byte hasStacks = dat.readByte();
        int[] items = new int[0];
        if (hasStacks > 0)
        {
            items = new int[hasStacks];
            for (int i = 0; i < items.length; i++)
            {
                items[i] = dat.readInt();
            }
        }
        World world = magnetchem.proxy.getClientWorld();
        TileEntity te = world.getTileEntity(x, y, z);
        TileEntityMagneticInductionFurnace icte = (TileEntityMagneticInductionFurnace) te;
        icte.handlePacketData(gauss, items);
	}
}
