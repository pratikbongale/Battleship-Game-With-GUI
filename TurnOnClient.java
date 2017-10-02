/*
 * TurnOnClient.java
 *
 * Version:
 *      $Id TurnOnClient.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import javax.swing.*;          //This is the final package name.
import java.awt.*;
import java.awt.event.*;

/**
 * TurnOnClient is the main class to be invoked in order to connect to server and start a game.
 * It takes the player's name and creates the mainscreen for this player.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

class TurnOnClient {
	public static void main(String args[]) {

		// lookandFeel code copied from HP's website
		String lookAndFeel;
    	lookAndFeel=UIManager.getCrossPlatformLookAndFeelClassName();
    	try {
                UIManager.setLookAndFeel( lookAndFeel);
            } catch (Exception e) { }

 		Frame frame = new JFrame("Welcome Player");
		
      	String username;
		boolean validInput = false;

		while(!validInput) {
			username = (String)JOptionPane.showInputDialog( frame, "Please enter your name:", "Hello Player", JOptionPane.PLAIN_MESSAGE, null, null, "");
			if ((username != null) && (username.length() > 0)) {
			    System.out.println("user typed : " + username);
			    validInput = true;
			    frame.dispose();
			    
			    Client c = new Client(username);	// make a new frame and make it visible

			    c.setMinimumSize(new Dimension(450, 450));
				c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    //c.pack();
			    new Thread(c).start();
			    c.setLocationRelativeTo(null);
			    c.setSize(new Dimension(650, 550));
				c.setVisible(true);
				//System.out.println("Ending main thread of client");
			    
			} else {
				JOptionPane.showMessageDialog(frame, "Invalid input");
			}	
		}
		
	}	
}