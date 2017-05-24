/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class TcpClient extends NetworkFather {
	Socket socket;
	DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;
	TcpClient(String remoteAddress){
		try {
			this.socket = new Socket(remoteAddress,serverPort);
			this.dataInputStream = new DataInputStream(socket.getInputStream());
			this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
			this.authFunc();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	private void authFunc() throws IOException{
		this.write("ACK");
	}
	public String read() throws IOException{
		return this.dataInputStream.readUTF();
	}
	public void write(String str) throws IOException{
		this.dataOutputStream.writeUTF(str);
		return ;
	}
	public void close() throws IOException{
		if (dataInputStream != null)
			dataInputStream.close();
		if (dataOutputStream != null)
			dataOutputStream.close();
		if (socket != null)
			socket.close();
	}
}
