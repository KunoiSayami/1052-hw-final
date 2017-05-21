/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

class GameServer extends NetworkFather{
	int playerCount;
	int onlinePlayer;
	String[] clientAddressStrings;
	String[] onlineAddressStrings;
	ReentrantLock threadLock;
	String[] syncStrings;
	GameServer(int _playerCount){
		this.playerCount = _playerCount;
		this.clientAddressStrings = new String[playerCount];
		this.threadLock = new ReentrantLock();
		this.syncStrings = new String[playerCount];
	}
	public void listenRequest(){
		ServerSocket serverSocket = null;
		ExecutorService threadExecuteor = Executors.newCachedThreadPool();
		try{
			serverSocket = new ServerSocket(serverPort);
			while (true){
				Socket socket = serverSocket.accept();
				threadExecuteor.execute(new RequestThread(
					socket,this.onlineAddressStrings,this.threadLock
					));
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
		finally{
			if (threadExecuteor != null)
				threadExecuteor.shutdown();
			if (serverSocket != null)
				try{
					serverSocket.close();
				}
				catch (IOException e){
					e.printStackTrace();
				}
		}
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
		private String [] clientOnlineStrings;
		private int clientID;
		ReentrantLock threadLock;
		public RequestThread(Socket _clientSocket , String [] _clientOnlineStrings,ReentrantLock _threadLock)
			throws IndexOutOfBoundsException {
			this.clientID = 0;
			this.clientSocket = _clientSocket;
			this.clientOnlineStrings = _clientOnlineStrings;
			this.threadLock = _threadLock;
			this.checkAvaliable();
		}
		void checkAvaliable() throws IndexOutOfBoundsException{
			for (int i = 0 ; i < this.clientOnlineStrings.length; i++)
				if (this.clientSocket.getInetAddress().getHostAddress() == this.clientOnlineStrings[i]){
					this.clientID = i;
					break;
				}
			if (this.clientID == 0){
				IndexOutOfBoundsException indexOutOfBoundsException =
					new IndexOutOfBoundsException();
				throw indexOutOfBoundsException;
			}
		}

		@Override
		public void run(){
			DataInputStream dataInputStream = null;
			DataOutputStream dataOutputStream = null;
			try {
				dataInputStream = new DataInputStream(this.clientSocket.getInputStream());
				dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());
				while (true){
					this.threadLock.lock();
				}
			}
			catch (IOException e){
				e.printStackTrace();
			}
			finally{
				try{
					if (dataInputStream != null) dataInputStream.close();
					if (dataOutputStream != null) dataOutputStream.close();
					if (this.clientSocket != null && !this.clientSocket.isClosed())
						this.clientSocket.close();
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}

