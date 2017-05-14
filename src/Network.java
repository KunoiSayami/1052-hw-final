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
	UdpServer(boolean _isClientMode){
		super("0.0.0.0");
		this.isClientMode = _isClientMode;
	}
	@Override
	public void run() throws Exception{
		byte buffer[] = new byte[buffSize];
		DatagramSocket socket = new DatagramSocket(port, this.inetAddress);
		while (true){
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String packmsg = new String(buffer , 0, packet.getLength());
			System.out.printf("%s:%s\n",packet.getAddress(),packmsg);
		}
	}
	void clientProcdata(){
		
	}
	void serverProcdata(){

	}
}

