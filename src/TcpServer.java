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

public class TcpServer {
	public static final int LISTEN_PORT = 9487;
	public void listenRequest(){
		ServerSocket serverSocket = null;
		ExecutorService threadExecuteor = Executors.newCachedThreadPool();
		try{
			serverSocket = new ServerSocket(LISTEN_PORT);
			System.out.println("Server listening requests...");
			while (true){
				Socket socket = serverSocket.accept();
				threadExecuteor.execute(new RequestThread(socket));
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
	public static void main(String[] args) {
		TcpServer server = new TcpServer();
		server.listenRequest();
	}
	class RequestThread implements Runnable{
		private Socket clientSocket;
		public RequestThread(Socket clientSocket){
			this.clientSocket = clientSocket;
		}
		@Override
		public void run(){
			System.out.printf("有%s連線進來\n",clientSocket.getRemoteSocketAddress());
			DataInputStream input = null;
			DataOutputStream output = null;
			try{
				input = new DataInputStream(this.clientSocket.getInputStream());
				output = new DataOutputStream(this.clientSocket.getOutputStream());
				while (true){
					output.writeUTF(String.format("Hi, %s\n", clientSocket.getRemoteSocketAddress()));
					output.flush();
					break;
				}
			}
			catch (IOException e){
				e.printStackTrace();
			}
			finally{
				try{
					if (input != null) input.close();
					if (output != null) output.close();
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
