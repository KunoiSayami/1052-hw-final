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
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

class GameServer extends NetworkFather{
	int playerCount;
	String[] clientAddressStrings;
	FlagedString[] socketSendMsg,socketReceiveMsg;
	Hand[] hand;
	static OnlineUser onlineUser;
	static CardStore cardStore = null;
	boolean[] isPlayerGG;
	GameServer(int _playerCount){
		this.playerCount = _playerCount;
		this.clientAddressStrings = new String[this.playerCount];
		this.socketSendMsg = new FlagedString[this.playerCount];
		this.socketReceiveMsg = new FlagedString[this.playerCount];
		this.hand = new Hand[this.playerCount];
		if (cardStore == null){
			cardStore = new CardStore(this.playerCount);
			onlineUser = new OnlineUser(this.playerCount);
		}
	}
	public void listenRequest(){
		ServerSocket serverSocket = null;
		ExecutorService threadExecuteor = Executors.newCachedThreadPool(),
						checkExecutor = Executors.newCachedThreadPool();
		try{
			serverSocket = new ServerSocket(serverPort);
			while (true){
				Socket socket = serverSocket.accept();
				int clientID;
				if ((clientID = this.__getClientID__(socket)) == this.playerCount){
					socket.close();
					continue;
				}
				onlineUser.write(socket);
				/*{
					IndexOutOfBoundsException indexOutOfBoundsException = 
						new IndexOutOfBoundsException();
					throw indexOutOfBoundsException;
				}*/
				threadExecuteor.execute(new RequestThread(
					socket,socketSendMsg[clientID],socketReceiveMsg[clientID]
					));
				checkExecutor.execute(()->{
					try {
						while (this.socketReceiveMsg[clientID].getFlag())
							Thread.sleep(1000);
						this.__callNewCard__(clientID);
					}
					catch (InterruptedException e){}
				});
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
	private int __getClientID__(Socket socket) throws IOException{
		int clientID;
		for (clientID = -1; clientID < this.playerCount &&
			clientAddressStrings[++clientID]!=socket.getInetAddress().getHostAddress(););
		return clientID;
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
	
	String __getMsgString__(Hand h){
		String str = String.format("HAND\\n\\n%d\\n%d\\n", h.getPoint(),h.getEachPointVector().size());
		for (Integer x:h.getEachPointVector())
			str += String.format("%d\\n", x);
		return str;
	}

	public void createServer(){
		while (true){
			for (int i = 0;i < this.playerCount; i++ ){
				while (hand[i].addPoint(cardStore.getNextPoint()));
				this.socketSendMsg[i].setMsg(this.__getMsgString__(hand[i]));
			}
			/*for (Hand h:hand){
				while(h.addPoint(cardStore.getNextPoint()));
			}*/
		}
	}
	private void __checkReceiveMsg__(Socket socket){
		try {
			int clientID = this.__getClientID__(socket);
			this.__checkReceiveMsg__(clientID);
		} catch (Exception e){}
	}
	private void __checkReceiveMsg__(int clientID) throws InterruptedException{
		String[] strgroup = socketReceiveMsg[clientID].getMsg().split("\\n\\n");
		if (strgroup[0] == "NEEDNEW")
			this.__callNewCard__(clientID);
	}
	private void __callNewCard__(Socket socket){
		try{
			int clientID = this.__getClientID__(socket);
			this.__callNewCard__(clientID);
		} catch (Exception e){}
	}
	private void __callNewCard__(int clientID) throws InterruptedException{
		int pointNew = cardStore.getNextPoint();
		hand[clientID].addPoint(pointNew);
		String str = String.format("NEW\\n\\n%d", pointNew);
		while (socketSendMsg[clientID].getFlag())
			Thread.sleep(500);
		socketSendMsg[clientID].setMsg(str);
	}
	class RequestThread implements Runnable{
		private Socket clientSocket;
		FlagedString socketSendMsg,socketReceiveMsg;
		DataInputStream dataInputStream = null;
		DataOutputStream dataOutputStream = null;
		public RequestThread(
			Socket _clientSocket,
			FlagedString _socketSendMsg,FlagedString _socketReceiveMsg
			)
			throws IndexOutOfBoundsException {
				this.clientSocket = _clientSocket;
				this.socketSendMsg = _socketSendMsg;
				this.socketReceiveMsg = _socketReceiveMsg;
		}

		@Override
		public void run(){
			try {
				this.dataInputStream = new DataInputStream(this.clientSocket.getInputStream());
				this.dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());
				if (this.dataInputStream.readUTF() == "SYN"){
					this.dataOutputStream.writeUTF("ACK");
					/** 
					 * DataInputStream.readUTF will block our code. So, I create a new thread to
					 * process read action.
					 * REFERENCE : https://coderanch.com/t/278776/java/readInt-DataInputStream-class-block
					 */
					Runnable runnable = () -> {
						try {
							this.socketReceiveMsg.setMsg(this.dataInputStream.readUTF());
							
						} catch (Exception e){}
					};
					Thread thread = new Thread(runnable);
					thread.start();
					while (true){
						while (!socketSendMsg.getFlag())
							try{Thread.sleep(1000);} catch (InterruptedException e){}
						this.dataOutputStream.writeUTF(socketSendMsg.getMsg());
					}
				}
			}
			catch (IOException e){
				e.printStackTrace();
			}
			finally {
				this.close();
			}
		}
		public void close(){
			try {
				if (this.clientSocket != null)
					this.clientSocket.close();
				if (this.dataInputStream != null)
					this.dataInputStream.close();
				if (this.dataOutputStream != null)
					this.dataOutputStream.close();
			} catch (IOException e){
				// TODO : RESERVE FOR FEATURE
			}
		}
	}
}


class Hand{
	int point;
	Vector<Integer> eachPoint;
	Hand(){
		eachPoint = new Vector<Integer>();
		this.point = 0;
	}
	public boolean addPoint(int _point){
		this.point+= _point;
		eachPoint.add(new Integer(_point));
		return this.point>17?true:false;
	}
	public void clearPoint(){
		this.point = 0;
		eachPoint.clear();
		return ;
	}
	public Vector<Integer> getEachPointVector(){
		return this.eachPoint;
	}
	public int getPoint(){
		return this.point;
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

class FlagedString{
	String msg;
	boolean needSend;
	FlagedString(){
		this.msg = "";
		this.needSend = false;
	}
	void reset(){
		this.msg = "";
		this.needSend = false;
	}
	public void setMsg(String _msg){
		this.msg = _msg;
		this.needSend = true;
	}
	public boolean getFlag(){
		return needSend;
	}
	public String getMsg(){
		String tmpMsg = this.msg;
		this.reset();
		return tmpMsg;
	}
}
