/*
 * AttackGridListener.java
 *
 * Version:
 *      $Id AttackGridListener.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import javax.swing.*;          //This is the final package name.
import java.awt.*;
import java.awt.event.*;

/**
 * AttackGridListener is a listener to actions performed on the attack grid of a player.
 * It is available for input only when the player has his turn to play.
 * It reacts to events performed on buttons of attackgrid.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */



class AttackGridListener implements ActionListener {

	Player player;
	JLabel nameLabel;
	Client frame;
	static int row;
	static int col;

	AttackGridListener(Player p, JLabel l, Client f) {
		player = p;
		nameLabel = l;
		frame = f;
		row = -1;
		col = -1;
	}

	public void actionPerformed(ActionEvent e) {

		if(frame.accessToAttackGridAllowed) {
			// getactioncommand - so got position
			String pos = e.getActionCommand();
			row = Integer.parseInt(pos.substring(0,1));
			col = Integer.parseInt(pos.substring(1,2));
			System.out.println(row + " " + col);	
		} else {
			frame.showMessage("Attack grid is locked, reasons:\n" +
								"1. Fleet is not completely initialized\n" +
								"2. Opponent is playing his turn");
		}
		
	}
}