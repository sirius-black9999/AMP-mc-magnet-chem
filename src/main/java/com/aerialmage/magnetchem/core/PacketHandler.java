package com.aerialmage.magnetchem.core;

import com.aerialmage.magnetchem.statics.ClassFinder;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import ibxm.Player;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*******************************************************************************
* Copyright (c) 2012 cpw.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
*
* Contributors:
*     cpw - initial API and implementation
******************************************************************************/


public class PacketHandler extends FMLNetworkHandler {

	static HashMap<Byte, BasePacket> packets;
	public PacketHandler()
	{
		packets = new HashMap<Byte, BasePacket>();
		Set<Class<?>> classes;
		try {
			classes = ClassFinder.getClasses("AMP.mod.core.packets");

			BasePacket[] packs = new BasePacket[classes.size()];
			int counter = 0;
			for(Class<?> c : classes)
			{
					packs[counter++] = (BasePacket) c.newInstance();
			}
			for(BasePacket pack : packs)
			{
				if(pack != null)
				packets.put(pack.ID, pack);
			}
			System.out.println("succesfully listed "+packets.size()+" classes");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

//	@Override
    public void onPacketData(NetworkManager network, Packet packet, Player player)
    {
        PacketBuffer buffer = new PacketBuffer(network.channel().alloc().buffer());
        try {
            packet.readPacketData(buffer);

        byte id = buffer.readByte();
        BasePacket target = packets.get(id);
        if(target != null)
        {
        	target.parseData(buffer.array());
        }
        else
        {
        	System.out.println("no target found for id: "+id);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
    }
	public static Packet getPacket(Object ent) {
		BasePacket target = null;
		//System.out.println("comparing "+packets.size()+" packet types");
		for(Map.Entry<Byte, BasePacket> entry : packets.entrySet())
		{
			if(entry.getValue().appliesTo(ent))
			{
				target = entry.getValue();
				break;
			}
		}
		if(target != null)
		{
			Packet pkt = new S3FPacketCustomPayload("AMP", target.convertData(ent));
//            PacketBuffer buffer = new PacketBuffer(NetworkManager.)
//            pkt.writePacketData();
//			pkt.channel = "AMP";
//			pkt.data = target.convertData(ent);
//			pkt.length = pkt.data.length;
//			pkt.isChunkDataPacket = true;
			return pkt;
		}
		else
		{
			System.out.println("please write a packet handler for "+ent.toString());
			return null;
		}
	}
//	public static void SendPacketServerToAllClients(Packet p)
//	{
//		PacketDispatcher.sendPacketToAllPlayers(p);
//        net.minecraftforge.common.network.ForgeMessage
//	}
//	public static void SendPacketServerToAllClients(Object o)
//	{
//		PacketDispatcher.sendPacketToAllPlayers(getPacket(o));
//	}
//	public static void SendPacketServerToClient(Packet p, Player player)
//	{
//		PacketDispatcher.sendPacketToPlayer(p, player);
//	}
//	public static void SendPacketServerToClient(Object o, Player player)
//	{
//		PacketDispatcher.sendPacketToPlayer(getPacket(o), player);
//	}
//	public static void SendPacketServerToClient(Packet p, int dimID)
//	{
//		PacketDispatcher.sendPacketToAllInDimension(p, dimID);
//	}
//	public static void SendPacketServerToClient(Object o, int dimID)
//	{
//		PacketDispatcher.sendPacketToAllInDimension(getPacket(o), dimID);
//	}
//	public static void SendPacketClientToServer(Packet p)
//	{
//		PacketDispatcher.sendPacketToServer(p);
//	}
//	public static void SendPacketClientToServer(Object o)
//	{
//		System.out.println("sending "+o.toString()+" to server");
//		PacketDispatcher.sendPacketToServer(getPacket(o));
//	}
}
