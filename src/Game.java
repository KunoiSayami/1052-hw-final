/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */


import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Game extends JFrame{
	int aiLevel;
	static Random random = new Random();
	JPanel undertitleJPanel,mainJPanel;
	JButton aboutButton,newGameButton;
	JTextField statusArea;
	public Game(){
		super("Scissors stone cloth");
		this.setAilevel();
		this.initUnderTitleJPanel();
		this.initMainJPanel();
		this.setSize(300,300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	void initUnderTitleJPanel(){
		this.undertitleJPanel = new JPanel();
		this.undertitleJPanel.setLayout(new GridLayout(1,3));
		//this.undertitleJPanel.setSize(100, 100);

		this.statusArea = new JTextField("normal");
		this.statusArea.setHorizontalAlignment(JTextField.CENTER);
		this.statusArea.setEditable(false);
		//this.statusArea.requestFocus();
		this.statusArea.setFocusable(false);
		this.undertitleJPanel.add(this.statusArea);

		this.newGameButton = new JButton("New Game"); //NEW GAME!
		// TODO : New game action
		this.undertitleJPanel.add(newGameButton);

		this.aboutButton = new JButton("About");
		this.undertitleJPanel.add(this.aboutButton);

		this.add(this.undertitleJPanel);
	}

	void initMainJPanel(){
		
	}

	private void setAilevel(){
		String[] options={"Low Level","High Level","Exit"};
		int result = JOptionPane.showOptionDialog(null,
						"Please select AI level You want",
						"Select AI Level",
						JOptionPane.DEFAULT_OPTION,
	  					JOptionPane.INFORMATION_MESSAGE,null,
						options,"Low Level");
		if (result == 2 || result == -1)
			System.exit(0);
		switch (result){
			case 2:
			case -1:
				/**User select exit */
				System.exit(0);
			default:
				this.aiLevel = result;
		}
	}
	private int callNext(){
		return random.nextInt(3);
	}
}
