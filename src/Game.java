/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */

import java.util.Random;
import java.util.*;

class _cardStore{
	static int card[],step,player;
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
		card = new int[52];
		rand = new Random();
		player = players;
		this.initCard();
	}
	public int getNextPoint(){
		if (step+player*2>52)
			this.initCard();
		return ((card[step++]+1)%13)>10?10:card[step-1];
	}
	public void Debug_showCardSz(){
		Arrays.sort(card);
		for (int i=0;i<52;i++)
			System.out.printf("%d ",card[i]);
	}
}

public class Game {
	_cardStore cardStore;
}
