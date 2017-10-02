/*
 * Fleet.java
 *
 * Version:
 *      $Id Fleet.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.io.*;

/**
 * Fleet object belongs to every player, it stores the fleet created by this player.
 * Fleet has ship objects in it.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

class Fleet implements Serializable {
	
	final int numberOfShips = 4;					// number of ships is a fleet
	Ship ship[];									// ship array
	
	Fleet() {
			
		int shipType;
		ship = new Ship[numberOfShips];
		for(int i = 0; i < numberOfShips; i++ ) {
			shipType = i;
			ship[i] = new Ship(shipType);				// create ships
		}
	}

	void setShipPosition(int startR, int endR, int startC, int endC, int shipNumber) {
		
		ship[shipNumber].setStartIndexForRow(startR);
		ship[shipNumber].setStartIndexForCol(startC);
		ship[shipNumber].setEndIndexForRow(endR);
		ship[shipNumber].setEndIndexForCol(endC);

	}

	public boolean isAfloat() { 
		boolean alive = false;
		for( Ship s : ship ) {
			if( s.isAlive() ) {
				alive = true;
				break;
			}
		}

		return alive;
	}

	boolean hasShipAt( int r, int c ) {
		boolean hasShip = false;

		for( Ship s : ship ) {
			
			if( s.checkShipBoundaries(r,c) ) {		// returns true if the given point is within any of the ships boundaries
				hasShip = true;
													// mark this row and column as hit in the main ocean
			}
		}

		return hasShip;
	}

}
