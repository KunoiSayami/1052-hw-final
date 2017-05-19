
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
	String[] clientAddressStrings;
	String[] onlineAddressStrings;
	MainServer(int _playerCount){
		this.playerCount = _playerCount;
		this.clientAddressStrings = new String[playerCount];
	}
	public void insertClient(String clientAddress){
		for (int i = 0;i < this.playerCount ; i++)
			if (this.clientAddressStrings[i] == null){
				this.clientAddressStrings[i] = clientAddress;
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
		public RequestThread(Socket clientSocket){
			this.clientSocket = clientSocket;
		}
		@Override
		public void run(){
			
		}
	}
}

