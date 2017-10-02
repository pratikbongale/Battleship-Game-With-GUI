/*
 * Player.java
 *
 * Version:
 *      $Id Player.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.io.*;

/**
 * Player object holds references to its ocean and its fleet.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

class Player implements Serializable {

	private String name;											// Player's name
	Ocean ocean;													// Ocean assigned to player
	Fleet fleet;
	
	public Player( Ocean aOcean,  String name ) {
		this.ocean = aOcean;
		this.name = name;
		
	}

	void setFleet(Fleet f) {
		fleet = f;
	}

	String getName() {
		return name;
	}

	String attack( int x, int y ) {			// method implementation of attack() in PlayGame inteface

		if( this.fleet.hasShipAt( x, y ) ) {		//checks if there is a ship at x,y
			return "hit";
		} else {
			return "miss";
		}

	}
}