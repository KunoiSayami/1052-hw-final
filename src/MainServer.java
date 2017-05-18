
/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.net.Socket;

class MainServer extends TcpServer{
	int playerCount;
	int onlinePlayer;
	String[] clientAddresStrings;
	MainServer(int _playerCount){
		this.playerCount = _playerCount;
		this.clientAddresStrings = new String[playerCount];
	}
	public void insertClient(String clientAddress){
		for (int i = 0;i < this.playerCount ; i++)
			if (this.clientAddresStrings[i] == null){
				this.clientAddresStrings[i] = clientAddress;
				return ;
			}
		IndexOutOfBoundsException indexOutOfBoundsException = 
			new IndexOutOfBoundsException();
		throw indexOutOfBoundsException;
	}
	public void insertClient(String szClientAddress[]){
		for (String i : szClientAddress)
			this.insertClient(i);
	}
	
	class RequestThread implements Runnable{
		private Socket clientSocket;
		String[] onlinePlayerStrings;
		public RequestThread(Socket clientSocket){
			this.clientSocket = clientSocket;
		}
		@Override
		public void run(){
			
		}
	}
}

