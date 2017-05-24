/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Network {
	
}

class UdpServer extends UdpServerEx{
	boolean isClientMode;
	String[] addressStore;
	int index;
	String targetAddr;
	UdpServer(boolean _isClientMode,int playerCount) throws Exception{
		super();
		this.isClientMode = _isClientMode;
		this.addressStore = new String[playerCount];
		index = 0;
	}

	@SuppressWarnings("resource")
	public void run() throws Exception{
		byte buffer[] = new byte[buffSize];
		DatagramSocket socket = new DatagramSocket(serverPort, this.inetAddress);
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
				if (serverProcdata(packet,packmsg)){
					this.addressStore[this.index++] = packet.getAddress().getHostAddress();
				}
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

	@SuppressWarnings("unused")
	boolean serverProcdata(DatagramPacket packet,String msg) throws Exception{
		UdpClient udpClient = null;
		if (msg == "SYN"){
			udpClient = new UdpClient(packet.getAddress().getHostAddress(),serverPort,"ACK");
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

//A new class that can send udp message immediately
class UdpClient extends UdpClientEx{
	String msg;
	public UdpClient(String pServer, int pPort, String mMsg) throws Exception{
		super(pServer,pPort);
		this.send(mMsg);
	}
}

