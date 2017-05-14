/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.*;

class _cardStore{
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
	public _cardStore(int players){
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

public class Game extends JFrame{
	_cardStore cardStore;
	static final int maxplayer = 4;
	int playerCount;
	Game(){
		super("21 point");
		this.playerCount = this.gametypechoose();
		cardStore = new _cardStore(playerCount);
		if (this.playerCount > 1)
			this.chooseServerType();
	}
	void createServer(){

	}
	void searchServer(){

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
}
