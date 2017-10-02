/*
 * PersonalGridListener.java
 *
 * Version:
 *      $Id PersonalGridListener.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import javax.swing.*;          //This is the final package name.
import java.awt.*;
import java.awt.event.*;

/**
 * PersonalGridListener is a listener to actions performed on the ship grid of a player.
 * It is available for input only when the player initializes the grid to set up his fleet.
 * It reacts to events performed on buttons of shipgrid.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

class PersonalGridListener implements ActionListener {

	Player player;
	JLabel msgLabel;
	int currentShip = 0;
	Client frame;

	PersonalGridListener(Player p, JLabel l, Client f) {
		player = p;
		msgLabel = l;
		frame = f;
	}

	public void actionPerformed(ActionEvent e) {

		if(frame.accessToShipGridAllowed) {
			if(currentShip < player.fleet.numberOfShips) {
				// getactioncommand - so got position
				int startRow=0, endRow=0;
				int startCol=0, endCol=0;
				int startR, endR;
				int startC, endC;
				Point xy;

				String pos = e.getActionCommand();
				startRow = Integer.parseInt(pos.substring(0,1));
				startCol = Integer.parseInt(pos.substring(1,2));
				System.out.println("start pos " + startRow);

				Object[] possibilities = {"left", "right", "up", "down"};
				String s = (String)JOptionPane.showInputDialog(
				                    frame,		// check if this works
				                    "Intended orientation of this ship (left, right, up, down) :",
				                    "Specify ship's orientation",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    possibilities,
				                    "left");

				
				String shipName = player.fleet.ship[currentShip].getName();
				int shipSize = player.fleet.ship[currentShip].getSize();	
				
				//If a string was returned, say so.
				if ((s != null) && (s.length() > 0)) {
				    
					switch(s) {
						case "left":
							
							endCol = startCol - shipSize + 1;
							endRow = startRow;
							break;

						case "right":
							
							endCol = startCol + shipSize -1;	
							endRow = startRow;
							break;

						case "up":

							endRow = startRow - shipSize + 1;
							endCol = startCol;
							break;

						case "down":

							endRow = startRow + shipSize - 1;
							endCol = startCol;
							break;	

						default:
							endRow = -1;
							endCol = -1;
					
					}	// end of switch

					if(endRow != -1 && endCol != -1) {

						xy = new Point(endRow,endCol);
						System.out.println(endRow + " " + endCol);
						if( checkBoundaries(xy) ) {	// check if the given end point is within ocean

							startR = Math.min(startRow,endRow);
							endR = Math.max(startRow,endRow);
							startC = Math.min(startCol,endCol);
							endC = Math.max(startCol,endCol);
							

							if ( checkShipOverlap( player, startR, endR, startC, endC ) ) {	// check if ship overlaps with other ships

								if ( checkShipProximity( player, startR, endR, startC, endC ) ) {	// check if ship touches any other ship

									// everythings perfect, accept this point.
									setPoistion(player, startR, endR, startC, endC, currentShip);
									setColorOnGameBoard(startR, endR, startC, endC, e);
									currentShip++;		
									if(currentShip < player.fleet.numberOfShips) {
										shipName = player.fleet.ship[currentShip].getName();
										shipSize = player.fleet.ship[currentShip].getSize();
										frame.nameLabel.setText(player.getName().toUpperCase() + " Positioning Ship: " + shipName + " of size " + shipSize);
									} else {
										frame.nameLabel.setText(player.getName().toUpperCase());
									}
									
									
						
								} else
									JOptionPane.showMessageDialog(frame,"Ship is in proximity of another ship...");	

							} else 
								JOptionPane.showMessageDialog(frame,"Ship overlaps with another ship...");
							
						} else 
							JOptionPane.showMessageDialog(frame,"ship placed with this orientation goes out of bound, please try again..");
					
					} else 
						JOptionPane.showMessageDialog(frame,"Orientation incorrect, please rectify");

				} else 
					JOptionPane.showMessageDialog(frame, "Operation Canceled");
					
			} // end of if
		
		} else {
			frame.showMessage("You cannot change your ship positions!!!");
		}

		
		
	} // end of action performed

	void setColorOnGameBoard(int startR, int endR, int startC, int endC, ActionEvent e) {
		
		Component[] components = ((JButton)e.getSource()).getParent().getComponents();
		int index = 0;
		JButton barray[][] = new JButton[10][10];
		for(int i = startR; i <= endR; i++) {
			for(int j = startC; j <= endC; j++) {
				components[i*10+j].setBackground(Color.BLUE);		

			}
		}
		
	}

	boolean checkBoundaries(Point p) {
		if( p.getRow() < player.ocean.minRow || p.getRow() > player.ocean.maxRow ||
				p.getCol() < player.ocean.minCol || p.getCol() > player.ocean.maxCol )
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
			if( startR < p.ocean.minRow )
				startR++;
			if( startC < p.ocean.minCol )
				startC++;
			if( endR > p.ocean.maxRow )
				endR--;
			if( endC > p.ocean.maxCol )
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

}