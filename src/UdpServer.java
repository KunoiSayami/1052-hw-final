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
 
public class UdpServer {
    int port;    // 連接埠
 
    public static void main(String args[]) throws Exception {
        UdpServer server = new UdpServer(5555); // 建立 UdpServer 伺服器物件。
        server.run();                           // 執行該伺服器。
    }
 
    public UdpServer(int pPort) {
        port = pPort;                            // 設定連接埠。
    }
 
    public void run() throws Exception {
        final int SIZE = 8192;                    // 設定最大的訊息大小為 8192.
        byte buffer[] = new byte[SIZE];            // 設定訊息暫存區
        for (int count = 0; ; count++) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            DatagramSocket socket = new DatagramSocket(port);         // 設定接收的 UDP Socket.
            socket.receive(packet);                                    // 接收封包。
            String msg = new String(buffer, 0, packet.getLength());    // 將接收訊息轉換為字串。
            System.out.println(count+" : receive = "+msg);                    // 印出接收到的訊息。
            socket.close();                                            // 關閉 UDP Socket.
        }
    }
}
