/*
 * Client.java
 *
 * Version:
 *      $Id Client.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.rmi.*;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;          //This is the final package name.
import java.awt.*;
import java.awt.event.*;

/**
 * Client is a JFrame that represents the player's main screen,
 * It displays messages and also holds the logic to initialize the players fleet.
 * 
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

class Client extends JFrame implements Runnable {

	final static int minRow = 0;
	final static int minCol = 0;
	final static int maxRow = 9;
	final static int maxCol = 9;

	String playerName;
	Player player;
	AttackInterface rmiObj;
	JLabel nameLabel;
	JPanel personalGridPane;
	JPanel attackGridPane;
	JPanel mainPane;
	boolean personalGridReady = false;
	PersonalGridListener pgListener = null;
	AttackGridListener agListener = null;
	boolean changeOfTurn = true;
	boolean accessToAttackGridAllowed = false;
	boolean accessToShipGridAllowed = false;

	Client(String name) {
		playerName = name;
		try {
               rmiObj = new AttackInterfaceImplementation(minRow,maxRow,minCol,maxCol,this);    
        	   Naming.rebind("//localhost/IamABattleshipPlayer"+playerName, rmiObj);
        	   System.out.println("IamABattleshipPlayer" + playerName + " bound in registry");

				// create ocean, player, fleet and ships
				Ocean oceanForPlayer = new Ocean( minRow, maxRow, minCol, maxCol );		// make a 10 X 10 ocean
				player = new Player(oceanForPlayer, playerName);
				Fleet fleetForPlayer = new Fleet();
				player.setFleet(fleetForPlayer);
				
				pgListener = new PersonalGridListener(player, nameLabel, this);
				agListener = new AttackGridListener(player, nameLabel, this);
        	   	
        	   	Component contents = createComponents();
        	   	getContentPane().add(contents);
        
        } catch (Exception e ) {
	           System.out.println(e.toString());
	           System.exit(1);
        }
	}

	public Component createComponents() {

		// top part of mainPane
		nameLabel = new JLabel("Hello " + playerName.toUpperCase(),JLabel.CENTER);
		nameLabel.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 35));

		JLabel shipGridLabel = new JLabel(playerName.toUpperCase() + "'s Ship Grid",JLabel.LEFT);
		shipGridLabel.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 25));

		JLabel attackGridLabel = new JLabel(playerName.toUpperCase() + "'s Attack Grid",JLabel.LEFT);
		attackGridLabel.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 25));
		
		JPanel namePane = new JPanel();
        namePane.setLayout(new BorderLayout());		
        namePane.add(nameLabel, BorderLayout.NORTH);
        
        // bottom part of mainPane
        personalGridPane = createButtonGrid();
        attackGridPane = createButtonGrid();

        personalGridPane.setEnabled(false);
        attackGridPane.setEnabled(false);

        // mainPane using gridbag layout
        mainPane = new JPanel();
        mainPane.setLayout(new GridBagLayout());		// highlevel layout
		
		GridBagConstraints c = new GridBagConstraints();
		
		// set contraints for label's panel

		c.gridwidth = 2;
		c.gridx = 0;
	    c.gridy = 0;
	    c.weightx = 0.2;
	    c.weighty = 0.2;
        mainPane.add(namePane, c);	// only has the name of player displayed on top, spans that row

        // set the name for grids

        c.gridwidth = 1;
		c.gridx = 0;
	    c.gridy = 1;
	    c.weightx = 0.2;
	    c.weighty = 0.2;
        mainPane.add(shipGridLabel, c);

        c.gridwidth = 1;
		c.gridx = 1;
	    c.gridy = 1;
	    c.weightx = 0.2;
	    c.weighty = 0.2;
        mainPane.add(attackGridLabel, c);
        // set for left grid
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.8;
        c.weighty = 0.8;
        mainPane.add(personalGridPane, c);	// has grid to set up ships

        // set for right grid
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.8;
        c.weighty = 0.8;
        mainPane.add(attackGridPane, c);	// has grid to track attacks

        return mainPane;

	}

	public void run() {
			
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		JOptionPane.showMessageDialog(mainPane, "Hey " + playerName + " Lets set up your fleet ...\n" +
										"Please find your ship grid on the left and \n" + 
										"select appropriate positions for your ships one by one as instructed.\n" +
										"Instructions are displayed in the top portion of your screen" );

		String shipName = player.fleet.ship[pgListener.currentShip].getName();
		int shipSize = player.fleet.ship[pgListener.currentShip].getSize();	
		nameLabel.setText(playerName.toUpperCase() + " Positioning Ship: " + shipName + "  of size " + shipSize);

		personalGridPane.setEnabled(true);
		accessToShipGridAllowed = true;
        attackGridPane.setEnabled(false);

		// wait until all the values are set
		while(pgListener.currentShip < player.fleet.numberOfShips) {
			try {
				Thread.sleep(1000);
				//System.out.println("sleeping in client thread waiting for all ships , cs : " + pgListener.currentShip);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		// disable the grids
		personalGridPane.setEnabled(false);
		accessToShipGridAllowed = false;
        attackGridPane.setEnabled(false);

		JOptionPane.showMessageDialog(mainPane, "You are all set " + playerName + " Waiting for your opponent to finish set up...");


		try {
			AddPlayerToGame serverObject = (AddPlayerToGame)Naming.lookup("//localhost/IamABattleshipServer");

			String ack = serverObject.addPlayer(player);				// remote call ask server to add this player to game
			System.out.println("Recieved acknowledgement from server : " + ack);

		} catch (Exception e) {
			System.out.println("addPlayer exception : " + e.getMessage());
			e.printStackTrace();
		}
		// now send this all set up and initialized player to server and wait.

		synchronized(this) {
			try {
				this.wait();			// wait until server gives a go ahead
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		}

		System.out.println("client thread dead");

		// now the game has begun and client only needs to send his co-ordinates
		
		
	}
	
	JPanel createButtonGrid() {

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout( maxRow+1, maxCol+1 ));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JButton block;
		for( int row = minRow; row <= maxRow; row++ ) {
			for ( int col = minCol; col <= maxCol; col++ ) {
				block = new JButton();
				block.setActionCommand(""+row+col);
				block.setMinimumSize(new Dimension(20,20));
				if(!personalGridReady) {
					block.addActionListener(pgListener);	
				} else {
					block.addActionListener(agListener);	
				}
				
				buttonPane.add(block);

			}
		}

		personalGridReady = true;

		return buttonPane;
	}

	boolean checkBoundaries(Point p) {
		if( p.getRow() < minRow || p.getRow() > maxRow ||
				p.getCol() < minCol || p.getCol() > maxCol )
			return false;
		else
			return true;
	}	

	boolean checkShipOverlap( Player p, int startR, int endR, int startC, int endC ) {
		for(int x = startR; x <= endR; x++ )
			for(int y = startC; y <= endC; y++ )
				if( p.ocean.oceanGrid[x][y] == 1 )
					return false;

		return true;
	}

	boolean checkShipProximity( Player p, int startR, int endR, int startC, int endC ) {
		startR--;
		startC--;

		endR++;
		endC++;


		if( (checkBoundaries( new Point(startR, startC) ) && checkBoundaries( new Point(endR, endC) )) == false ) { 
			if( startR < minRow )
				startR++;
			if( startC < minCol )
				startC++;
			if( endR > maxRow )
				endR--;
			if( endC > maxCol )
				endC--;
				
		}

		for(int x = startR; x <= endR; x++ )
			for(int y = startC; y <= endC; y++ ) {
				if( p.ocean.oceanGrid[x][y] == 1 )
					return false;
			}

		return true;
	}

	void setPoistion( Player p, int startR, int endR, int startC, int endC , int shipNumber) {
		
		for(int x = startR; x <= endR; x++ )
			for(int y = startC; y <= endC; y++ )
				p.ocean.oceanGrid[x][y] = 1;
		
		p.fleet.setShipPosition(startR, endR, startC, endC, shipNumber);

	}

	
	boolean isAttackPosSet() {
		if( AttackGridListener.row != -1 && AttackGridListener.col != -1 ) 
			return true;
		else
			return false;
	}

	void showMessage(String s) {
		
		JOptionPane.showMessageDialog(mainPane, s);
	}

	void setOutcomeColor(int r, int c, String msg) {
		
		Component[] components = attackGridPane.getComponents();
		
		if(msg.equals("miss"))
			components[r*10+c].setBackground(Color.RED);		
		else
			components[r*10+c].setBackground(Color.GREEN);		
		
	}

	void markHit(int row, int col) {
		Component[] components = personalGridPane.getComponents();
		
		components[row*10+col].setBackground(Color.BLACK);

	}

	
	void notifySelf() {
		try {
			
			synchronized(this) {
					this.notifyAll();
			}
			//System.out.println("Notifying client");

		} catch (Exception e) {
			System.out.println(e.toString());
           	System.exit(1);	
		}
	}

	


}





