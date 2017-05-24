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

import java.util.Arrays;
import java.util.Random;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

class GameServer extends NetworkFather{
	int playerCount;
	String[] clientAddressStrings;
	ReentrantLock threadLock;
	String[] syncStrings;
	Hand[] hand;
	static OnlineUser onlineUser;
	static CardStore cardStore = null;
	GameServer(int _playerCount){
		this.playerCount = _playerCount;
		this.clientAddressStrings = new String[this.playerCount];
		this.threadLock = new ReentrantLock();
		this.syncStrings = new String[this.playerCount];
		this.hand = new Hand[this.playerCount];
		if (cardStore == null)
			cardStore = new CardStore(this.playerCount);
	}
	public void listenRequest(){
		ServerSocket serverSocket = null;
		ExecutorService threadExecuteor = Executors.newCachedThreadPool();
		try{
			serverSocket = new ServerSocket(serverPort);
			while (true){
				Socket socket = serverSocket.accept();
				threadExecuteor.execute(new RequestThread(
					socket,onlineUser,this.threadLock
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
	
	public void createServer(){
		
	}

	class RequestThread implements Runnable{
		private Socket clientSocket;
		//private String [] clientOnlineStrings;
		private OnlineUser onlineUser;
		private int clientID;
		ReentrantLock threadLock;
		public RequestThread(Socket _clientSocket,
			OnlineUser _onlineUser,
			ReentrantLock _threadLock)
			throws IndexOutOfBoundsException {
				this.clientID = 0;
				this.clientSocket = _clientSocket;
				//this.clientOnlineStrings = _clientOnlineStrings;
				this.onlineUser = _onlineUser;
				this.threadLock = _threadLock;
				this.checkAvaliable();
		}
		void checkAvaliable() throws IndexOutOfBoundsException{
			for (int i = 0 ; i < this.onlineUser.getOnlineUserStrings().length; i++)
				if (this.clientSocket.getInetAddress().getHostAddress() == this.onlineUser.getOnlineUserStrings()[i]){
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


class Hand{
	int point;
	Hand(){
		this.point = 0;
	}
	public boolean addPoint(int _point){
		this.point+= _point;
		return this.point>17?true:false;
	}
	public void clearPoint(){
		this.point = 0;
		return ;
	}
}



class CardStore{
	static int card[] = null,step,player;
	static Random rand;
	//Remove Ghost because 21 point no need
	private void initCard(){
		step = 0;
		boolean tmpsz[] = new boolean[52];
		for (int i=0,tmp;i<52;i++){
			while (tmpsz[tmp = rand.nextInt(52)]);
			card[i] = tmp;
			tmpsz[tmp] = true;
		}
	}
	public CardStore(int players){
		// https://goo.gl/XVQGpD
		if (card != null)
			return ;
		card = new int[52];
		rand = new Random();
		player = players;
		this.initCard();
	}
	public int getNextPoint(){
		if (step+player*3+4>52)
			this.initCard();
		return ((card[step++]+1)%13)>10?10:card[step-1];
	}
	public void Debug_showCardSz(){
		Arrays.sort(card);
		for (int i=0;i<52;i++)
			System.out.printf("%d ",card[i]);
	}
}

class OnlineUser{
	int onlineUserCount;
	String[] onlineUserStrings;
	int playerLimit;
	OnlineUser(int totalPlayer){
		this.onlineUserCount = 0;
		this.playerLimit = totalPlayer;
		this.onlineUserStrings = new String[this.playerLimit];
	}
	public void write(String remoteAddress){
		boolean is_repeat = false;
		for (String x:this.onlineUserStrings)
			if (remoteAddress == x){
				is_repeat = true;
				break;
			}
		if (is_repeat)
			return ;
		onlineUserStrings[onlineUserCount] = remoteAddress;
		onlineUserCount++;
	}
	public void write(Socket remoteSocket){
		this.write(remoteSocket.getInetAddress().getHostAddress());
	}
	public int getOnlineUserCount() {
		return onlineUserCount;
	}
	public String[] getOnlineUserStrings() {
		return onlineUserStrings;
	}

}
