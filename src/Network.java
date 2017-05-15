/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;

public class Network {
	
}

class UdpServer extends UdpServerEx{
	boolean isClientMode;
	String[] addressStore;
	int index;
	String targetAddr;
	UdpServer(boolean _isClientMode,int playerCount){
		super("0.0.0.0");
		this.isClientMode = _isClientMode;
		this.addressStore = new String[playerCount];
		index = 0;
	}

	@SuppressWarnings("resource")
	public void run() throws Exception{
		byte buffer[] = new byte[buffSize];
		DatagramSocket socket = new DatagramSocket(port, this.inetAddress);
		while (this.check()){
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String packmsg = new String(buffer , 0, packet.getLength());
			//System.out.printf("%s:%s\n",packet.getAddress(),packmsg);
			//return isClientMode?clientProcdata():serverProcdata();
			
			if (this.isClientMode)
				if (clientProcdata(packet,packmsg))
					break;
			else
				if (serverProcdata(packet,packmsg))
					this.addressStore[this.index++] = packet.getAddress().getHostAddress();
		}
	}

	boolean check(){
		return this.isClientMode?true:this.index < this.addressStore.length;
	}

	boolean clientProcdata(DatagramPacket packet,String msg){
		if (msg == "ACK"){
			this.targetAddr = packet.getAddress().getHostAddress();
			return true;
		}
		else return false;
	}

	boolean serverProcdata(DatagramPacket packet,String msg) throws Exception{
		UdpClient udpClient = null;
		if (msg == "SYN"){
			udpClient = new UdpClient(packet.getAddress().getHostAddress(),port,"ACK");
			return true;
		}
		return false;
	}
	
	String getTarget() throws RuntimeException{
		RuntimeException runtimeException = new RuntimeException("targetAddr must be inited");
		if (this.targetAddr == null)
			throw runtimeException;
		return this.targetAddr;
	}
}


class UdpClient extends UdpClientEx{
	String msg;
	public UdpClient(String pServer, int pPort, String mMsg) throws Exception{
		super(pServer,pPort);
		this.send(mMsg);
	}
}
