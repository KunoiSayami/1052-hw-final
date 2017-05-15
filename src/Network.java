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
		DatagramSocket socket = new DatagramSocket(this.port, this.inetAddress);
		while (this.check()){
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String packmsg = new String(buffer , 0, packet.getLength());
			//System.out.printf("%s:%s\n",packet.getAddress(),packmsg);
			//return isClientMode?clientProcdata():serverProcdata();
			
			if (this.isClientMode)
				clientProcdata(packet,packmsg);
			else
				serverProcdata(packet,packmsg);
		}
	}

	boolean check(){
		return this.isClientMode?true:this.index < this.addressStore.length;
	}
	void clientProcdata(DatagramPacket packet,String msg){
		
	}

	void serverProcdata(DatagramPacket packet,String msg){

	}
	
	String getTarget(){
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
		this.msg = mMsg;
	}
	public void send(){
		sendEx(this.msg);
	}
}
