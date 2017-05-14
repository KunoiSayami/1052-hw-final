/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.io.*;
import java.net.*;
 
// 1. 本程式必須與 UdpClient.java 程式搭配執行，先執行本程式再執行 UdpClient。
// 2. 執行方法 : java UdpServer
 
public class UdpServerEx {
	static final int port = 9487;    // 連接埠
	InetAddress inetAddress; 
	static final int buffSize = 8192;
	public static void main(String args[]) throws Exception {
		UdpServerEx server = new UdpServerEx(args[1]); // 建立 UdpServer 伺服器物件。
		server.run();                           // 執行該伺服器。
	}
 
	public UdpServerEx(String inetaddress) {
		try {
			this.inetAddress = InetAddress.getByName(inetaddress);
		}
		catch (UnknownHostException e){
			e.printStackTrace();
		}
		finally{
			try{
				this.inetAddress = InetAddress.getByName("0.0.0.0");
			}
			catch (UnknownHostException e){
				e.printStackTrace();
			}
		}
	}
 
	@SuppressWarnings("resource")
	public void run() throws Exception {
	//	final int SIZE = 8192;                    // 設定最大的訊息大小為 8192.
		byte buffer[] = new byte[buffSize];            // 設定訊息暫存區
		DatagramSocket socket = new DatagramSocket(port,this.inetAddress); 
		for (int count = 0; ; count++) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					// 設定接收的 UDP Socket.
			socket.receive(packet);                                    // 接收封包。
			String msg = new String(buffer, 0, packet.getLength());    // 將接收訊息轉換為字串。
			//System.out.println(count+" : receive = "+msg);                    // 印出接收到的訊息。
			System.out.printf("Connect from: %s\n",packet.getAddress());
			//socket.close();                                            // 關閉 UDP Socket.
		}
	}
}