/*
 * Ship.java
 *
 * Version:
 *      $Id Ship.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.util.*;

/**
 * Ship represents a ship storing its start and end co-ordinates on the ocean. 
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

import java.io.*;

class Ship implements Serializable {

	private String name;
	private int size;
	private int startIndexForRow;
	private int endIndexForRow;
	private int startIndexForCol;
	private int endIndexForCol;
	private boolean afloat;

	Ship( int type ) {

		afloat = true;

		switch( type ){
			case 0:
				name = "Carrier";
				size = 5;
				break;
			case 1:
				name = "Battleship";
				size = 4;
				break;
			case 2:
				name = "Cruiser";
				size = 3;
				break;
			case 3:
				name = "Destroyer";
				size = 2;
				break;
			default:
				name = "ship";

		}
	}

	boolean checkShipBoundaries( int r, int c ) {

		if ( startIndexForRow <= r && endIndexForRow >= r && startIndexForCol <= c && endIndexForCol >= c ) {
			size--;							// ship got hit and its size now decreases
			if ( size == 0 ) {				// if ship size becomes 0, means ship has sunk
				afloat = false;
			}
			return true;
		} else {
			return false;
		}
	}

	boolean isAlive() {
		return afloat;
	}

	String getName() {
		return name;
	}

	int getSize() {
		return size;
	}

	void setAfloat(boolean value) {
		afloat = value;
	}

	void setStartIndexForRow( int i ) {
		startIndexForRow = i;
	}

	void setEndIndexForRow( int i ) {
		endIndexForRow = i;
	}

	int getStartIndexForRow() {
		return startIndexForRow;
	}

	int getEndIndexForRow() {
		return endIndexForRow;
	}

	void setStartIndexForCol( int i ) {
		startIndexForCol = i;
	}

	void setEndIndexForCol( int i ) {
		endIndexForCol = i;
	}

	int getStartIndexForCol() {
		return startIndexForCol;
	}

	int getEndIndexForCol() {
		return endIndexForCol;
	}
}
