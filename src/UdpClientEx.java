/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.net.*;
 
// 1. 本程式必須與 UdpServer.java 程式搭配執行，先執行 UdpServer 再執行本程式。
// 2. 本程式必須有一個參數，指定伺服器的 IP。
// 用法範例： java UdpClient 127.0.0.1
 
public class UdpClientEx extends Thread {
	int port;            // port : 連接埠
	InetAddress server; // InetAddress 是 IP, 此處的 server 指的是伺服器 IP
	String msg;            // 欲傳送的訊息，每個 UdpClient 只能傳送一個訊息。
 
	public static void main(String args[]) throws Exception {
		for (int i=0; i<100; i++) {
			// 建立 UdpClient，設定傳送對象與傳送訊息。
			UdpClientEx client = new UdpClientEx(args[0], 5555, "UdpClient : "+i+"th message");
			client.run(); // 啟動 UdpClient 開始傳送。
		}
	}
 
	public UdpClientEx(String pServer, int pPort, String pMsg) throws Exception {
		port = pPort;                             // 設定連接埠
		server = InetAddress.getByName(pServer); // 將伺服器網址轉換為 IP。
		msg = pMsg;                                 // 設定傳送訊息。
	}
 
	public void run() {
	  try {
		byte buffer[] = msg.getBytes();                 // 將訊息字串 msg 轉換為位元串。
		// 封裝該位元串成為封包 DatagramPacket，同時指定傳送對象。
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, server, port); 
		DatagramSocket socket = new DatagramSocket();    // 建立傳送的 UDP Socket。
		socket.send(packet);                             // 傳送
		socket.close();                                 // 關閉 UDP socket.
	  } catch (Exception e) { e.printStackTrace(); }    // 若有錯誤產生，列印函數呼叫堆疊。
	}
}