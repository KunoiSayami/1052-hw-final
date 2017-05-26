/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Game extends JFrame{
	static final int maxplayer = 4;
	int playerCount;
	TcpClient tcpClient;
	GameServer gameServer;
	Thread gameServerThread;
	String targetServerAddress;
	Game(){
		super("21 point");
		this.playerCount = this.gametypechoose();
		if (this.playerCount > 1)
			this.chooseServerType();
		gameServer = null;
		gameServerThread = null;
	}
	void createServer(){
		Runnable gameServerDaemon = () ->{
			this.gameServer = new GameServer(this.playerCount);
		};
		this.gameServerThread = new Thread(gameServerDaemon);
		gameServerThread.start();
		try {
			tcpClient = new TcpClient(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e){
			e.printStackTrace();
		}
		return ;
	}
	void searchServer(){
		try {
			UdpServer udpServer = new UdpServer(true, playerCount);
			new UdpClient("255.255.255.255", 9487, "ACK");
			//Sleep 5 seconds to wait server response
			Thread.sleep(5000);
			this.targetServerAddress = udpServer.getTarget();
			tcpClient = new TcpClient(this.targetServerAddress);
		}
		catch (RuntimeException e){
			JOptionPane.showMessageDialog(null, "Cannot find server!\nProgram will now exit.",
				"Client Error",JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
			//abort();
			System.exit(5);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	void chooseServerType(){
		String[] optionsServerType = {"Create server","LAN server"};
		int result = JOptionPane.showOptionDialog(null,
												"Create or connect to other server",
												"Server type",
												JOptionPane.DEFAULT_OPTION,
												JOptionPane.INFORMATION_MESSAGE,
												null,
												optionsServerType,
												optionsServerType[0]
												);
		if (result == -1) System.exit(0);
		if (result == 1)
			this.searchServer();
		else
			this.createServer();
		return ;
	}
	int gametypechoose(){
		String[] options={"Solo player","Multiplayer","Exit"};
		int result = JOptionPane.showOptionDialog(null,"Please choose Types of Game","21 point",JOptionPane.DEFAULT_OPTION,
	  										JOptionPane.INFORMATION_MESSAGE,null,
											options,"Solo player");
		//JOptionPane.showMessageDialog(null, ""+opt);
		if (result == 2 || result == -1)
			System.exit(0);
		return result == 0? 1 : this.playerchooseEx();
	}
	int playerchooseEx(){
		String playerstr = JOptionPane.showInputDialog("Please input player count");
		String rematch = "^[2-9]$";
		if (playerstr == null) System.exit(0);
		Pattern r = Pattern.compile(rematch);
		Matcher m = r.matcher(playerstr);
		if (!m.find()){
			JOptionPane.showMessageDialog(null, "Check your input.\n"
					+ "Error Message: Regular expressions not match(\""+rematch+"\")");
			System.exit(1);
		}
		int players = Integer.parseInt(playerstr);
		if (players >= maxplayer){
			JOptionPane.showMessageDialog(null, "Check your input.\n"
					+ "Error Message: Input more than "+maxplayer);
			System.exit(2);
		}
		return players;
	}
	static String authorString = "Copyright (C) 2017 Too-Naive\nAll rights reserved.";
	static void aboutMe(){
			// TODO : RESERVED BY FEATURE
			/*try{
					File f = new File("authorSay.txt");
					Scanner input = new Scanner(f);
					String str = "";
					while (input.hasNext())
							 str += input.next();
					input.close();
			} catch (Exception e){}*/
			String me = authorString + AboutMe.rawAboutMe;
			JOptionPane.showMessageDialog(null, me);
		}
	}
}
