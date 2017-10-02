/*
 * Ocean.java
 *
 * Version:
 *      $Id Ocean.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.io.*;

/**
 * Ocean is created and assigned to each player, it holds two grids, one is general purpose where he places his fleet
 * other one is used to keep a track of his hits and misses.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

import java.io.*;

class Ocean implements Serializable {
	final int minRow;											
	final int maxRow;
	final int minCol;
	final int maxCol;

	int oceanGrid[][];											// array to represent all co-ordinates of a ocean
	char attackGrid[][];										// array for each user to keep a track of his previous attacks

	public Ocean( int minR, int maxR, int minC, int maxC ) {

		this.minRow = minR;
		this.maxRow = maxR;
		this.minCol = minC;
		this.maxCol = maxC;

		oceanGrid = new int[maxRow+1][maxCol+1];				// all values will have the value 0 (default value of int)
		attackGrid = new char[maxRow+1][maxCol+1];

		int row,col;
		for( row = minRow; row <= maxRow; row++ ) {
			for ( col = minCol; col <= maxCol; col++ ) {
				attackGrid[row][col] = '.';
			}
		}
		
	}

	void mark( int row, int col, String attack ) {
		if( attack == "hit" ) {
			attackGrid[row][col] = 'H';		
		}else {
			attackGrid[row][col] = 'M';
		}
	}
}