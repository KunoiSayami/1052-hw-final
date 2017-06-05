/*
 * This source code was published under GPL v3
 *
 * Copyright (C) 2017 Too-Naive
 *
 */


import java.awt.BorderLayout;
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
	JPanel undertitleJPanel,gameJPanel,actionJPanel;
	JButton aboutButton,newGameButton;
	JTextField statusField,gameStatusField;
	JButton scissorsButton,stoneButton,clothButton;
	public Game(){
		super("Scissors stone cloth");
		this.setAilevel();
		this.setLayout(new BorderLayout(30,0));
		this.setSize(500,300);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.initUnderTitleJPanel();
		this.initMainJPanel();
		this.initActionJPanel();

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	void initUnderTitleJPanel(){
		this.undertitleJPanel = new JPanel();
		this.undertitleJPanel.setLayout(new GridLayout(1,3,10,20));
		//this.undertitleJPanel.setSize(100, 100);

		this.statusField = new JTextField("normal");
		this.statusField.setHorizontalAlignment(JTextField.CENTER);
		this.statusField.setEditable(false);
		//this.statusField.requestFocus();
		this.statusField.setFocusable(false);
		this.undertitleJPanel.add(this.statusField);

		this.newGameButton = new JButton("New Game"); //NEW GAME!
		// TODO : New game action
		this.undertitleJPanel.add(newGameButton);

		this.aboutButton = new JButton("About");
		this.undertitleJPanel.add(this.aboutButton);

		this.add(this.undertitleJPanel,BorderLayout.NORTH);
	}

	void initMainJPanel(){
		this.gameJPanel = new JPanel();
		this.gameJPanel.setLayout(new GridLayout(1,1));
		
		this.gameStatusField = new JTextField("This is game text field");
		this.gameStatusField.setHorizontalAlignment(JTextField.CENTER);
		this.gameStatusField.setEditable(false);
		this.gameStatusField.setFocusable(false);
		this.gameJPanel.add(this.gameStatusField);

		this.add(this.gameJPanel,BorderLayout.CENTER);
	}

	void initActionJPanel(){
		this.actionJPanel = new JPanel();
		this.actionJPanel.setLayout(new GridLayout(1,3,10,20));

		this.scissorsButton = new JButton("Scissors");
		this.actionJPanel.add(this.scissorsButton);

		this.stoneButton = new JButton("Stone");
		this.actionJPanel.add(this.stoneButton);

		this.clothButton = new JButton("Cloth");
		this.actionJPanel.add(this.clothButton);

		this.add(this.actionJPanel,BorderLayout.SOUTH);
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
